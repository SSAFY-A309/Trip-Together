package com.ssafy.twinklebank.account.repository;

import com.ssafy.twinklebank.account.domain.Account;
import com.ssafy.twinklebank.account.domain.WithdrawalAgreement;
import com.ssafy.twinklebank.application.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WithdrawalAgreementRepository extends JpaRepository<WithdrawalAgreement, Long> {
    Optional<WithdrawalAgreement> findByAccountAndApplication(Account account, Application application);
}
