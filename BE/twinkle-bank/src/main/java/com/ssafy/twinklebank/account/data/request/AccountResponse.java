package com.ssafy.twinklebank.account.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccountResponse(
    @JsonProperty("account_uuid") String accountUuid,
    @JsonProperty("account_num") String accountNum,
    double balance,
    String name,
	@JsonProperty("is_connected") boolean isConnected
) {
}