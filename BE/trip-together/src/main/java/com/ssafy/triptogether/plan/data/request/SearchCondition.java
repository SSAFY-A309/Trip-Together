package com.ssafy.triptogether.plan.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SearchCondition(
	Double latitude, // 위도
	Double longitude, // 경도
	Double latitude_delta,
	Double longitude_delta,
	String category,
	String keyword
) {
}
