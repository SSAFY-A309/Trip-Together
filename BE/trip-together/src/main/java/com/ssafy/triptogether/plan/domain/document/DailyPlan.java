package com.ssafy.triptogether.plan.domain.document;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record DailyPlan(
        Double dailyEstimatedBudget,
        LocalDate date,
        List<DailyPlanAttraction> dailyPlanAttractions
) {
}
