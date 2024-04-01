package com.ssafy.triptogether.member.repository.query;

import com.ssafy.triptogether.flashmob.domain.MemberFlashMob;

import java.util.Optional;

public interface MemberFlashmobRepositoryCustom {
    Optional<MemberFlashMob> findMemberFlashmobByFlashmobIdAndMemberId(long flashmobId, long memberId);
    boolean isMaster(long flashmobId, long memberId);
    Optional<MemberFlashMob> findMemberFlashmobByFlashmobIdNotInMemberId(long flashmobId, long memberId);
}
