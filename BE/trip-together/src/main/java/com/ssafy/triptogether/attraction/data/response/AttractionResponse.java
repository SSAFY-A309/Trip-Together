package com.ssafy.triptogether.attraction.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AttractionResponse(
	@JsonProperty("attraction_id") long attractionId,
	@JsonProperty("thumbnail_image_url") String thumbnailImageUrl,
	String name,
	String address,
	@JsonProperty("avg_rating") double avgRating,
	@JsonProperty("avg_price") double avgPrice,

	String latitude,
	String longitude,
	@JsonProperty("distance") double distance
) {
}