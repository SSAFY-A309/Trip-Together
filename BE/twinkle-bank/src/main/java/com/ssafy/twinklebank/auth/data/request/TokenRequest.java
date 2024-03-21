package com.ssafy.twinklebank.auth.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

// 일단은 access token 요청 시 id, password를 준다고 가정하고 구현 -> 코드 부분은 이 이후에 구현 예정
@Builder
public record TokenRequest(
	@NotNull
	@JsonProperty("client_id")
	String clientId,
	@NotNull
	@JsonProperty("code")
	String code,
	@NotNull
	@JsonProperty("user_id")
	String username,
	@NotNull
	@JsonProperty("password")
	String password
) {
}
