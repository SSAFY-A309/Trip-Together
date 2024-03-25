package com.ssafy.triptogether.auth.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record TokenResponse(
	@NotNull @JsonProperty("member_id")
	Long memberId,
	@NotNull @JsonProperty("username")
	String username,
	@JsonProperty("nickname")
	String nickname,
	@JsonProperty("image_url")
	String imageUrl,
	@JsonProperty("description")
	String description,
	@NotNull @JsonProperty("is_pin")
	Boolean isPin,
	@JsonProperty("token")
	TokenInfo tokenInfo
) {
}
