package com.ssafy.triptogether.infra.twinklebank.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record TwinkleBankAccountExchangeRequest(
	@JsonProperty("account_uuid")
	String accountUuid,
	@JsonProperty("type")
	String type,
	@JsonProperty("price")
	Double price,
	@JsonProperty("business_name")
	String businessName,
	@JsonProperty("address")
	String address
) {
}
