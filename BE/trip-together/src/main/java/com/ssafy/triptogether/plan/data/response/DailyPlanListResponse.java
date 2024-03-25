package com.ssafy.triptogether.plan.data.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.triptogether.attraction.domain.Nation;
import com.ssafy.triptogether.plan.domain.Status;

public record DailyPlanListResponse(
	@JsonProperty("plan_id") Long planId,
	@JsonProperty("start_region") Nation startRegion,
	@JsonProperty("start_at") LocalDate startAt,
	@JsonProperty("end_at") LocalDate endAt,
	String title,
	@JsonProperty("total_estimated_budget") Double totalEstimatedBudget,
	@JsonProperty("total_budget") Double totalBudget,
	String status
	) {
}
