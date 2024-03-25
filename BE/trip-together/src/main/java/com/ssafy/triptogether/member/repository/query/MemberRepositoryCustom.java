package com.ssafy.triptogether.member.repository.query;

import com.ssafy.triptogether.member.data.ProfileFindResponse;

import java.util.Optional;

public interface MemberRepositoryCustom {
    Optional<ProfileFindResponse> findProfileByMemberId(long memberId);
}
