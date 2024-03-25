package com.ssafy.triptogether.plan.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record PlanDetail(
        @NotNull @JsonProperty("daily_estimated_budget")
        Double dailyEstimatedBudget,
        @NotNull @JsonProperty("attractions")
        List<AttractionDetail> attractionDetails
) {
}
