package com.ssafy.triptogether.member.repository.query;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.triptogether.member.data.ProfileFindResponse;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.ssafy.triptogether.member.domain.QMember.member;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ProfileFindResponse> findProfileByMemberId(long memberId) {
        return Optional.ofNullable(
                queryFactory.select(Projections.constructor(ProfileFindResponse.class,
                                member.id,
                                member.imageUrl,
                                member.nickname,
                                member.description,
                                member.gender,
                                member.birth,
                                member.createdAt,
                                member.username
                        ))
                        .from(member)
                        .where(member.id.eq(memberId))
                        .fetchOne()
        );
    }
}
