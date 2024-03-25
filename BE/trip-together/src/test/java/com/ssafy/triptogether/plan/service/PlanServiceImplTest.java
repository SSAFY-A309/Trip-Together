//package com.ssafy.triptogether.plan.service;
//
//import com.ssafy.triptogether.attraction.domain.Attraction;
//import com.ssafy.triptogether.attraction.domain.Nation;
//import com.ssafy.triptogether.attraction.domain.Region;
//import com.ssafy.triptogether.attraction.repository.AttractionRepository;
//import com.ssafy.triptogether.attraction.repository.RegionRepository;
//import com.ssafy.triptogether.global.exception.exceptions.category.BadRequestException;
//import com.ssafy.triptogether.global.exception.exceptions.category.ForbiddenException;
//import com.ssafy.triptogether.global.exception.exceptions.category.NotFoundException;
//import com.ssafy.triptogether.member.domain.Gender;
//import com.ssafy.triptogether.member.domain.Member;
//import com.ssafy.triptogether.member.repository.MemberRepository;
//import com.ssafy.triptogether.plan.data.request.AttractionDetail;
//import com.ssafy.triptogether.plan.data.request.PlanDetail;
//import com.ssafy.triptogether.plan.data.request.PlansSaveRequest;
//import com.ssafy.triptogether.plan.domain.Plan;
//import com.ssafy.triptogether.plan.domain.PlanAttraction;
//import com.ssafy.triptogether.plan.repository.PlanAttractionRepository;
//import com.ssafy.triptogether.plan.repository.PlanRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.BDDMockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class PlanServiceImplTest {
//    @InjectMocks
//    PlanServiceImpl planService;
//    @Mock
//    PlanRepository planRepository;
//    @Mock
//    PlanAttractionRepository planAttractionRepository;
//    @Mock
//    MemberRepository memberRepository;
//    @Mock
//    RegionRepository regionRepository;
//    @Mock
//    AttractionRepository attractionRepository;
//
//    @Nested
//    @DisplayName("여행 계획 저장")
//    class PlansSaveTest {
//        long memberId = 1L;
//        Member member;
//        Region region;
//        Attraction attraction1, attraction2, attraction3;
//        Plan plan;
//        PlansSaveRequest plansSaveRequest, weirdPlansSaveRequest;
//
//        @BeforeEach
//        void setUp() {
//            member = Member.builder()
//                    .gender(Gender.MALE)
//                    .nickname("TestUser")
//                    .uuid("TestUser")
//                    .birth(LocalDate.now())
//                    .build();
//            region = Region.builder()
//                    .latitude("test")
//                    .longitude("test")
//                    .nation(Nation.UK)
//                    .cityName("test")
//                    .build();
//            attraction1 = Attraction.builder()
//                    .latitude("test1")
//                    .longitude("test1")
//                    .name("test1")
//                    .startAt(LocalTime.now())
//                    .endAt(LocalTime.now())
//                    .address("test1")
//                    .businessNum("test1")
//                    .region(region)
//                    .thumbnailImageUrl("test1")
//                    .build();
//            attraction2 = Attraction.builder()
//                    .latitude("test2")
//                    .longitude("test2")
//                    .name("test2")
//                    .startAt(LocalTime.now())
//                    .endAt(LocalTime.now())
//                    .address("test2")
//                    .businessNum("test2")
//                    .region(region)
//                    .thumbnailImageUrl("test2")
//                    .build();
//            attraction3 = Attraction.builder()
//                    .latitude("test3")
//                    .longitude("test3")
//                    .name("test3")
//                    .startAt(LocalTime.now())
//                    .endAt(LocalTime.now())
//                    .address("test3")
//                    .businessNum("test3")
//                    .region(region)
//                    .thumbnailImageUrl("test3")
//                    .build();
//            AttractionDetail attraction1 = AttractionDetail.builder()
//                    .attractionId(1L)
//                    .build();
//            AttractionDetail attraction2 = AttractionDetail.builder()
//                    .attractionId(2L)
//                    .build();
//            AttractionDetail attraction3 = AttractionDetail.builder()
//                    .attractionId(3L)
//                    .build();
//            PlanDetail planDetail1 = PlanDetail.builder()
//                    .sequence(1)
//                    .attractions(List.of(attraction1, attraction2))
//                    .dailyEstimatedBudget(1.0)
//                    .build();
//            PlanDetail planDetail2 = PlanDetail.builder()
//                    .sequence(2)
//                    .attractions(List.of(attraction1, attraction3))
//                    .dailyEstimatedBudget(1.0)
//                    .build();
//            plansSaveRequest = PlansSaveRequest.builder()
//                    .title("test")
//                    .startAt(LocalDate.now())
//                    .endAt(LocalDate.now())
//                    .startRegionId(1L)
//                    .estimatedBudget(2.0)
//                    .planDetails(List.of(planDetail1, planDetail2))
//                    .build();
//            weirdPlansSaveRequest = PlansSaveRequest.builder()
//                    .title("test")
//                    .startAt(LocalDate.now())
//                    .endAt(LocalDate.now().minusDays(1))
//                    .startRegionId(1L)
//                    .estimatedBudget(2.0)
//                    .planDetails(List.of(planDetail1, planDetail2))
//                    .build();
//            plan = Plan.builder()
//                    .title("test")
//                    .startAt(LocalDate.now())
//                    .endAt(LocalDate.now())
//                    .estimatedBudget(2.0)
//                    .member(member)
//                    .region(region)
//                    .build();
//        }
//
//        @Test
//        @DisplayName("계획 저장 성공")
//        void plansSaveSuccess() {
//            // given
//            given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(member));
//            given(regionRepository.findById(anyLong())).willReturn(Optional.ofNullable(region));
//            given(planRepository.existOverlappingPlan(member, plansSaveRequest.startAt(), plansSaveRequest.endAt())).willReturn(false);
//            given(planRepository.save(any(Plan.class))).willReturn(plan);
//            given(attractionRepository.findById(eq(1L))).willReturn(Optional.ofNullable(attraction1));
//            given(attractionRepository.findById(eq(2L))).willReturn(Optional.ofNullable(attraction2));
//            given(attractionRepository.findById(eq(3L))).willReturn(Optional.ofNullable(attraction3));
//            // when
//            planService.plansSave(memberId, plansSaveRequest);
//            // then
//            verify(memberRepository, times(1)).findById(memberId);
//            verify(regionRepository, times(1)).findById(anyLong());
//            verify(planRepository, times(1)).save(any(Plan.class));
//            verify(attractionRepository, times(4)).findById(anyLong());
//            verify(planAttractionRepository, times(4)).save(any(PlanAttraction.class));
//        }
//
//        @Test
//        @DisplayName("이미 같은 기간에 계획이 존재하는 경우")
//        void OverlappingPlan() {
//            // given
//            given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(member));
//            given(regionRepository.findById(anyLong())).willReturn(Optional.ofNullable(region));
//            given(planRepository.existOverlappingPlan(member, plansSaveRequest.startAt(), plansSaveRequest.endAt())).willReturn(true);
//            // when
//            assertThrows(BadRequestException.class, () -> {
//                planService.plansSave(memberId, plansSaveRequest);
//            });
//            // then
//            verify(memberRepository, times(1)).findById(memberId);
//            verify(regionRepository, times(1)).findById(anyLong());
//            verify(planRepository, times(1)).existOverlappingPlan(member, plansSaveRequest.startAt(), plansSaveRequest.endAt());
//        }
//
//        @Test
//        @DisplayName("시작 날짜가 종료 날짜보다 느린 경우")
//        void endAtOverStartAt() {
//            // given
//            given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(member));
//            given(regionRepository.findById(anyLong())).willReturn(Optional.ofNullable(region));
//            // when
//            assertThrows(BadRequestException.class, () -> {
//                planService.plansSave(memberId, weirdPlansSaveRequest);
//            });
//            // then
//            verify(memberRepository, times(1)).findById(memberId);
//            verify(regionRepository, times(1)).findById(anyLong());
//            verify(planRepository, times(0)).existOverlappingPlan(member, plansSaveRequest.startAt(), plansSaveRequest.endAt());
//        }
//    }
//
//    @Nested
//    @DisplayName("여행 계획 삭제")
//    class PlanDeleteTest {
//        long memberId = 1L;
//        long planId = 1L;
//        Plan plan;
//
//        @BeforeEach
//        void setUp() {
//            Member member = Member.builder()
//                    .gender(Gender.MALE)
//                    .nickname("TestUser")
//                    .uuid("TestUser")
//                    .birth(LocalDate.now())
//                    .build();
//            ReflectionTestUtils.setField(member, "id", memberId);
//            Region region = Region.builder()
//                    .latitude("test")
//                    .longitude("test")
//                    .nation(Nation.UK)
//                    .cityName("test")
//                    .build();
//            plan = Plan.builder()
//                    .title("test")
//                    .startAt(LocalDate.now())
//                    .endAt(LocalDate.now())
//                    .estimatedBudget(2.0)
//                    .member(member)
//                    .region(region)
//                    .build();
//        }
//
//        @Test
//        @DisplayName("여행 계획 삭제 성공")
//        void planDeleteSuccess() {
//            // given
//            given(planRepository.findById(anyLong())).willReturn(Optional.ofNullable(plan));
//            // when
//            planService.planDelete(memberId, planId);
//            // then
//            verify(planRepository, times(1)).findById(planId);
//            verify(planRepository, times(1)).delete(plan);
//        }
//
//        @Test
//        @DisplayName("존재하지 않는 여행 계획인 경우")
//        void planNotFound() {
//            // given
//            given(planRepository.findById(anyLong())).willReturn(Optional.empty());
//            // when & then
//            assertThrows(NotFoundException.class, () -> {
//                planService.planDelete(memberId, planId);
//            });
//            verify(planRepository, times(1)).findById(planId);
//        }
//
//        @Test
//        @DisplayName("권한이 없는 사용자의 접근인 경우")
//        void forbiddenMember() {
//            // given
//            given(planRepository.findById(anyLong())).willReturn(Optional.ofNullable(plan));
//            // when & then
//            assertThrows(ForbiddenException.class, () -> {
//                planService.planDelete(2L, planId);
//            });
//            verify(planRepository, times(1)).findById(planId);
//        }
//    }
//}
