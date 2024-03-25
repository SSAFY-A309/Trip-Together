package com.ssafy.triptogether.syncaccount.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SyncAccountSaveRequest(
        @NotBlank @JsonProperty("pin_num")
        String pinNum,
        @NotNull @JsonProperty("is_main")
        Boolean isMain,
        @NotBlank @JsonProperty("account_uuid")
        String bankAccountUuid
) {
}
