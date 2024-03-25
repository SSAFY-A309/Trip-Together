package com.ssafy.triptogether.syncaccount.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SyncAccountDeleteRequest(
        @NotBlank @JsonProperty("pin_num")
        String pinNum,
        @NotBlank @JsonProperty("account_uuid")
        String bankAccountUuid
) {
}
