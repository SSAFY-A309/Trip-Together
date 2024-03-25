package com.ssafy.twinklebank.account.repository;

import com.ssafy.twinklebank.account.domain.AccountHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountHistoryRepository extends JpaRepository<AccountHistory, Long> {
}
