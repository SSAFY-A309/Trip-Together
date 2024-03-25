package com.ssafy.triptogether.infra.twinklebank.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TwinkleTokenResponse(
	@NotBlank @JsonProperty("access_token")
	String accessToken
) {
}
