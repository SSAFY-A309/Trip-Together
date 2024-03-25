package com.ssafy.triptogether.infra.twinklebank.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record TwinkleBankAccountsLoadRequest(
        @JsonProperty("uuid")
        String uuid
) {
}
