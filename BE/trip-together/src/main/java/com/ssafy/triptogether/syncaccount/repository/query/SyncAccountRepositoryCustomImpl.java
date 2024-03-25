package com.ssafy.triptogether.syncaccount.repository.query;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.triptogether.syncaccount.data.response.SyncAccountsDetail;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ssafy.triptogether.syncaccount.domain.QSyncAccount.syncAccount;

@RequiredArgsConstructor
public class SyncAccountRepositoryCustomImpl implements SyncAccountRepositoryCustom {
    private final JPAQueryFactory queryFactory;

	/**
	 * 사용자의 연동 계좌 목록 조회
	 * @param memberId 요청자의 member_id
	 * @return 연동 계좌 목록
	 */
	@Override
	public List<SyncAccountsDetail> memberSyncAccountsLoad(long memberId) {
		return queryFactory.select(Projections.constructor(SyncAccountsDetail.class,
				syncAccount.uuid,
				syncAccount.num,
				syncAccount.name,
				syncAccount.isMain
			)).from(syncAccount)
			.where(syncAccount.member.id.eq(memberId))
			.fetch();
	}

    /**
     * 사용자의 연동 계좌가 한개라도 존재하는지 체크
     *
     * @param memberId 요청자의 member_id
     * @return 존재 여부
     */
    @Override
    public Boolean memberSyncAccountExist(long memberId) {
        return queryFactory
                .selectOne()
                .from(syncAccount)
                .where(syncAccount.member.id.eq(memberId))
                .fetchFirst() != null;
    }
}
