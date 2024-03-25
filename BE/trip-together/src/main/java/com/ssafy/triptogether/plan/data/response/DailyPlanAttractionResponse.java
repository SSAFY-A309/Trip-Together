package com.ssafy.triptogether.plan.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DailyPlanAttractionResponse(
        @JsonProperty("attraction_id") Long attractionId,
        Integer order,
        @JsonProperty("is_expired") Boolean isExpired
) {
}
