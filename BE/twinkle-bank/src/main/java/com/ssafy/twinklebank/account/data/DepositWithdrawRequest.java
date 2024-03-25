package com.ssafy.twinklebank.account.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.twinklebank.account.domain.Type;
import jakarta.validation.constraints.NotBlank;

public record DepositWithdrawRequest(
        @NotBlank @JsonProperty("account_uuid") String accountUuid,
        Type type,
        Double price,
        @NotBlank @JsonProperty("business_name") String businessName,
        @NotBlank String address
) {
}
