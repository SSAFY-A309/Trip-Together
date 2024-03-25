package com.ssafy.triptogether.infra.twinklebank.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record TwinkleBankAccountsLoadResponse(
        @JsonProperty("accounts")
        List<TwinkleBankAccountsDetail> twinkleBankAccountsDetails
) {
}
