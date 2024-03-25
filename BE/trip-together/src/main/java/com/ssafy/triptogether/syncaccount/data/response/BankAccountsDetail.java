package com.ssafy.triptogether.syncaccount.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record BankAccountsDetail(
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
