package com.ssafy.triptogether.member.utils;

import com.ssafy.triptogether.global.exception.exceptions.category.NotFoundException;
import com.ssafy.triptogether.global.exception.response.ErrorCode;
import com.ssafy.triptogether.member.domain.Member;
import com.ssafy.triptogether.member.repository.MemberRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberUtils {
    public static Member findByMemberId(MemberRepository repository, Long memberId) {
        return repository.findById(memberId)
                .orElseThrow(
                        () -> new NotFoundException("FindByMemberId", ErrorCode.UNDEFINED_MEMBER)
                );
    }
}
