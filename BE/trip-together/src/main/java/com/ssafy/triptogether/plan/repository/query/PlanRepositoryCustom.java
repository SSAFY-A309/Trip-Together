package com.ssafy.triptogether.plan.repository.query;

import com.ssafy.triptogether.member.domain.Member;
import com.ssafy.triptogether.plan.data.response.DailyPlanAttractionResponse;
import com.ssafy.triptogether.plan.data.response.DailyPlanResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PlanRepositoryCustom {
    boolean existOverlappingPlan(Member member, LocalDate startAt, LocalDate endAt);

    List<DailyPlanAttractionResponse> findAllDailyPlanByPlanId(long planId);

    Optional<DailyPlanResponse> findDetailPlanById(long planId);
}
