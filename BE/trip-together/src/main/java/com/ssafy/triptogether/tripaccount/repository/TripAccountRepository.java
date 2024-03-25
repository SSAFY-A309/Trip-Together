package com.ssafy.triptogether.tripaccount.repository;

import java.util.List;
import java.util.Optional;

import com.ssafy.triptogether.tripaccount.domain.TripAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripAccountRepository extends JpaRepository<TripAccount, Long> {
	List<TripAccount> findByMemberId(long memberId);
	Optional<TripAccount> findByMemberIdAndCurrencyId(long memberId, long currencyId);
}
