package com.ssafy.triptogether.plan.service;

import java.util.List;

import com.ssafy.triptogether.plan.data.response.DailyPlanListResponse;
import com.ssafy.triptogether.plan.data.response.PlanDetailFindResponse;

public interface PlanLoadService {
    PlanDetailFindResponse findPlanDetail(long planId);
    List<DailyPlanListResponse> findPlans(long memberId);
}
