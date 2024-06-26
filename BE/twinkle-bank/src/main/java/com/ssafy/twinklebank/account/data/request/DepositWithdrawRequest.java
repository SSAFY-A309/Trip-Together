package com.ssafy.twinklebank.account.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.twinklebank.account.domain.Type;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record DepositWithdrawRequest(
        @NotBlank @JsonProperty("account_uuid") String accountUuid,
        Type type,
        Double price,
        @NotBlank @JsonProperty("business_name") String businessName,
        @NotBlank String address
) {
}
