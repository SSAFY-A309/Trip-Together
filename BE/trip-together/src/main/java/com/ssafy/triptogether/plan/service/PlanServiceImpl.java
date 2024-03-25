package com.ssafy.triptogether.plan.service;

import com.ssafy.triptogether.attraction.domain.Region;
import com.ssafy.triptogether.attraction.repository.RegionRepository;
import com.ssafy.triptogether.attraction.utils.AttractionUtils;
import com.ssafy.triptogether.global.exception.exceptions.category.BadRequestException;
import com.ssafy.triptogether.global.exception.exceptions.category.ForbiddenException;
import com.ssafy.triptogether.global.exception.exceptions.category.NotFoundException;
import com.ssafy.triptogether.global.exception.response.ErrorCode;
import com.ssafy.triptogether.member.domain.Member;
import com.ssafy.triptogether.member.repository.MemberRepository;
import com.ssafy.triptogether.member.utils.MemberUtils;
import com.ssafy.triptogether.plan.data.request.AttractionDetail;
import com.ssafy.triptogether.plan.data.request.PlanDetail;
import com.ssafy.triptogether.plan.data.request.PlansSaveRequest;
import com.ssafy.triptogether.plan.data.response.DailyPlanAttractionResponse;
import com.ssafy.triptogether.plan.data.response.DailyPlanResponse;
import com.ssafy.triptogether.plan.data.response.PlanDetailFindResponse;
import com.ssafy.triptogether.plan.domain.Plan;
import com.ssafy.triptogether.plan.domain.document.DailyPlan;
import com.ssafy.triptogether.plan.domain.document.DailyPlanAttraction;
import com.ssafy.triptogether.plan.domain.document.DailyPlans;
import com.ssafy.triptogether.plan.repository.DailyPlansRepository;
import com.ssafy.triptogether.plan.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.ssafy.triptogether.global.exception.response.ErrorCode.PLAN_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanSaveService, PlanLoadService {
    // Repository
    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;
    private final RegionRepository regionRepository;
    private final DailyPlansRepository dailyPlansRepository;

    private static void planForbiddenCheck(Long memberId, Plan plan, String detailMessageKey) {
        if (!memberId.equals(plan.getMember().getId())) {
            throw new ForbiddenException(detailMessageKey, ErrorCode.FORBIDDEN_ACCESS_MEMBER);
        }
    }

    /**
     * 요청자의 여행 계획 저장
     *
     * @param memberId         요청자의 member_id
     * @param plansSaveRequest 요청자의 여행 계획
     */
    @Transactional
    @Override
    public void plansSave(Long memberId, PlansSaveRequest plansSaveRequest) {
        Member member = MemberUtils.findByMemberId(memberRepository, memberId);
        Region startRegion = AttractionUtils.findByRegionId(regionRepository, plansSaveRequest.startRegionId());

        if (plansSaveRequest.endAt().isBefore(plansSaveRequest.startAt())) {
            throw new BadRequestException("PlanSave", ErrorCode.PLAN_DATE_BAD_REQUEST);
        }

        if (existOverlappingPlan(plansSaveRequest, member)) {
            throw new BadRequestException("PlanSave", ErrorCode.PLAN_SAVE_BAD_REQUEST);
        }

        Plan plan = planSave(plansSaveRequest, startRegion, member);
        planAttractionSave(plansSaveRequest.planDetails(), plan);
    }

    /**
     * 요청한 여행 계획 삭제
     *
     * @param memberId 요청자의 member_id
     * @param planId   삭제하고 싶은 여행 계획의 plan_id
     */
    @Transactional
    @Override
    public void planDelete(Long memberId, Long planId) {
        Plan plan = planFindById(planId, "PlanDelete");
        planForbiddenCheck(memberId, plan, "PlanDelete");
        planRepository.delete(plan);
    }

    private Plan planFindById(Long planId, String detailMessageKey) {
        return planRepository.findById(planId)
                .orElseThrow(
                        () -> new NotFoundException(detailMessageKey, PLAN_NOT_FOUND)
                );
    }

    private boolean existOverlappingPlan(PlansSaveRequest plansSaveRequest, Member member) {
        return planRepository.existOverlappingPlan(member, plansSaveRequest.startAt(), plansSaveRequest.endAt());
    }

    private Plan planSave(PlansSaveRequest plansSaveRequest, Region startRegion, Member member) {
        Plan plan = Plan.builder()
                .title(plansSaveRequest.title())
                .startAt(plansSaveRequest.startAt())
                .endAt(plansSaveRequest.endAt())
                .estimatedBudget(plansSaveRequest.estimatedBudget())
                .region(startRegion)
                .member(member)
                .build();
        return planRepository.save(plan);
    }

    private void planAttractionSave(List<PlanDetail> planDetails, Plan plan) {
        List<DailyPlan> dailyPlans = new ArrayList<>();

        // loop daily plans
        IntStream.range(0, planDetails.size()).forEach(dailyPlanIdx -> {
            PlanDetail planDetail = planDetails.get(dailyPlanIdx);
            List<DailyPlanAttraction> dailyPlanAttractions = new ArrayList<>();

            // loop attraction details
            IntStream.range(0, planDetail.attractionDetails().size()).forEach(attractionIdx -> {
                AttractionDetail attractionDetail = planDetail.attractionDetails().get(attractionIdx);

                // create attraction details
                DailyPlanAttraction dailyPlanAttraction = DailyPlanAttraction.builder()
                        .order(attractionIdx)
                        .attractionId(attractionDetail.attractionId())
                        .attractionName(attractionDetail.attractionName())
                        .avgRating(attractionDetail.avgRating())
                        .avgPrice(attractionDetail.avgPrice())
                        .thumbnailImageUrl(attractionDetail.thumbnailImageUrl())
                        .address(attractionDetail.address())
                        .build();
                dailyPlanAttractions.add(dailyPlanAttraction);
            });

            // create daily plans
            DailyPlan dailyPlan = DailyPlan.builder()
                    .dailyEstimatedBudget(planDetail.dailyEstimatedBudget())
                    .dailyPlanAttractions(dailyPlanAttractions)
                    .date(plan.getStartAt().plusDays(dailyPlanIdx))
                    .build();
            dailyPlans.add(dailyPlan);
        });

        // create & save daily plan document
        DailyPlans plans = DailyPlans.builder().planId(plan.getId()).dailyPlans(dailyPlans).build();
        dailyPlansRepository.save(plans);
    }

    @Override
    public PlanDetailFindResponse findPlanDetail(long planId) {
        // find daily plans & plan detail
        List<DailyPlanAttractionResponse> dailyPlanAttraction = planRepository.findAllDailyPlanByPlanId(planId);
        DailyPlanResponse planDetail = planRepository.findDetailPlanById(planId)
                .orElseThrow(() -> new NotFoundException("PlanDetailFind", PLAN_NOT_FOUND));

        // set daily plans to plan detail & return
        return PlanDetailFindResponse.builder()
                .startRegion(planDetail.startRegion())
                .startAt(planDetail.startAt())
                .endAt(planDetail.endAt())
                .title(planDetail.title())
                .totalEstimatedBudget(planDetail.totalEstimatedBudget())
                .dailyPlans(dailyPlanAttraction)
                .build();
    }
}
