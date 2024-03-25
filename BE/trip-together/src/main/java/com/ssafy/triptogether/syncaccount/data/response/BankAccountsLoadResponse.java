package com.ssafy.triptogether.syncaccount.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record BankAccountsLoadResponse(
        @JsonProperty("accounts")
        List<BankAccountsDetail> bankAccountsDetails
) {
}
