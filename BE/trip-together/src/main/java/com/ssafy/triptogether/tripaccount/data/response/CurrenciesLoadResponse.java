package com.ssafy.triptogether.tripaccount.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record CurrenciesLoadResponse(
        @JsonProperty("currencies")
        List<CurrenciesLoadDetail> currenciesLoadDetail
) {
}
