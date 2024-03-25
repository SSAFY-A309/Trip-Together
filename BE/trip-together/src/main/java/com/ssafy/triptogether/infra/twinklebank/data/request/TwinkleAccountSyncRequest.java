package com.ssafy.triptogether.infra.twinklebank.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TwinkleAccountSyncRequest(
        @NotBlank @JsonProperty("account_uuid")
        String accountUuid
) {
}
