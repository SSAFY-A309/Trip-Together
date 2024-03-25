package com.ssafy.triptogether.infra.twinklebank.data.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.triptogether.member.domain.Gender;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TwinkleMemberInfoResponse(
	@NotBlank @JsonProperty("uuid")
	String memberUuid,
	@NotBlank @JsonProperty("name")
	String name,
	@JsonProperty("gender")
	Gender gender,
	@JsonProperty("birth")
	LocalDate birth
) {
}
