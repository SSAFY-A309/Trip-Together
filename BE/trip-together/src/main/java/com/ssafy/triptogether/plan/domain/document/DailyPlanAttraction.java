package com.ssafy.triptogether.plan.domain.document;

import lombok.Builder;

@Builder
public record DailyPlanAttraction(
        Integer order,
        Long attractionId,
        String attractionName,
        Integer avgRating,
        Double avgPrice,
        String thumbnailImageUrl,
        String address
) {
}
