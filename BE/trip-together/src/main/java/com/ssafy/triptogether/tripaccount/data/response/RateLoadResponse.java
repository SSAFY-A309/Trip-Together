package com.ssafy.triptogether.tripaccount.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record RateLoadResponse(
        @JsonProperty("rate")
        Double rate
) {
}
