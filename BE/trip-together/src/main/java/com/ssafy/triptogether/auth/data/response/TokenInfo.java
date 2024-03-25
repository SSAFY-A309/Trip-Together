package com.ssafy.triptogether.auth.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record TokenInfo(
	@JsonProperty("access_token")
	String accessToken,
	@JsonProperty("created_at")
	Long createdAt,
	@JsonProperty("expires_in")
	Integer expiresIn
) {
}
