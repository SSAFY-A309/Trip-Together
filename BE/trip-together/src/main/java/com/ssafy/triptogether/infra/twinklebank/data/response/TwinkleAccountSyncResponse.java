package com.ssafy.triptogether.infra.twinklebank.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record TwinkleAccountSyncResponse(
        @JsonProperty("account_uuid")
        String accountUuid,
        @JsonProperty("account_num")
        String accountNum,
        @JsonProperty("account_name")
        String accountName
) {
}
