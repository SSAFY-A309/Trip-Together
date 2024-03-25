package com.ssafy.triptogether.infra.twinklebank.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record TwinkleBankAccountsDetail(
        @JsonProperty("account_uuid")
        String uuid,
        @JsonProperty("account_num")
        String num,
        @JsonProperty("balance")
        Double balance,
        @JsonProperty("name")
        String name
) {
}
