package com.ssafy.triptogether.syncaccount.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record SyncAccountsLoadResponse(
        @JsonProperty("sync_accounts")
        List<SyncAccountsDetail> syncAccountsDetail
) {
}
