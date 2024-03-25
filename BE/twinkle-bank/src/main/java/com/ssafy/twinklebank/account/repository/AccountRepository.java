package com.ssafy.twinklebank.account.repository;

import com.ssafy.twinklebank.account.domain.Account;
import com.ssafy.twinklebank.account.repository.query.AccountRepositoryCustom;
import com.ssafy.twinklebank.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryCustom {
    Optional<Account> findAccountByUuid(String uuid);

    int countAccountByUuidAndMember(String accountUuid, Member member);
}
