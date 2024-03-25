package com.ssafy.triptogether.plan.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AttractionDetail(
        @NotNull @JsonProperty("attraction_id")
        Long attractionId,
        @NotNull @JsonProperty("attraction_name")
        String attractionName,
        @NotNull @JsonProperty("avg_rating")
        Integer avgRating,
        @NotNull @JsonProperty("avg_price")
        Double avgPrice,
        @NotBlank @JsonProperty("thumbnail_image_url")
        String thumbnailImageUrl,
        @NotBlank
        String address
) {
}
