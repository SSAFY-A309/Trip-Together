package com.ssafy.triptogether.infra.twinklebank.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TwinkleTokenRequest(
	@NotBlank @JsonProperty("secret_key")
	String secretKey
) {
}
