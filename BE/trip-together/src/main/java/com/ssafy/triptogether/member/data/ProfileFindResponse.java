package com.ssafy.triptogether.member.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.triptogether.member.domain.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProfileFindResponse(
        @JsonProperty("member_id") long memberId,
        @JsonProperty("image_url") String imageUrl,
        String nickname,
        String description,
        Gender gender,
        LocalDate birth,
        @JsonProperty("created_at") LocalDateTime createdAt,
        String username
) {
}
