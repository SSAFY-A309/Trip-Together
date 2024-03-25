package com.ssafy.triptogether.syncaccount.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record SyncAccountsDetail(
        @JsonProperty("account_uuid")
        String uuid,
        @JsonProperty("account_num")
        String accountNum,
        @JsonProperty("name")
        String name,
        @JsonProperty("is_main")
        Boolean isMain
) {
}
