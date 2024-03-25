package com.ssafy.triptogether.tripaccount.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.triptogether.tripaccount.domain.CurrencyCode;
import com.ssafy.triptogether.tripaccount.domain.CurrencyNation;
import lombok.Builder;

@Builder
public record CurrenciesLoadDetail(
        @JsonProperty("currency_code")
        CurrencyCode code,
        @JsonProperty("nation")
        CurrencyNation nation,
        @JsonProperty("nation_kr")
        String nationKr,
        @JsonProperty("unit")
        Integer unit
) {
}
