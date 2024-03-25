package com.ssafy.triptogether.tripaccount.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.triptogether.tripaccount.domain.CurrencyCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record TripAccountExchangeRequest(
	@NotBlank @JsonProperty("pin_num")
	String pinNum,
	@NotBlank @JsonProperty("account_uuid")
	String accountUuid,
	@NotNull @JsonProperty("to_currency_code")
	String toCurrencyCode,
	@NotNull @JsonProperty("from_currency_code")
	String fromCurrencyCode,
	@NotNull @JsonProperty("to_quantity")
	Double toQuantity,
	@NotNull @JsonProperty("from_quantity")
	Double fromQuantity
) {
}
