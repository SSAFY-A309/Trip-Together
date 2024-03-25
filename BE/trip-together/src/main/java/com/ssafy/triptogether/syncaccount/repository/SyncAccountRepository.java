package com.ssafy.triptogether.syncaccount.repository;

import com.ssafy.triptogether.syncaccount.domain.SyncAccount;
import com.ssafy.triptogether.syncaccount.repository.query.SyncAccountRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SyncAccountRepository extends JpaRepository<SyncAccount, Long>, SyncAccountRepositoryCustom {
    Optional<SyncAccount> findByMemberIdAndIsMain(long memberId, boolean isMain);

    Optional<SyncAccount> findByUuid(String uuid);
}
