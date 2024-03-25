package com.ssafy.triptogether.tripaccount.repository;

import com.ssafy.triptogether.tripaccount.domain.Currency;
import com.ssafy.triptogether.tripaccount.domain.CurrencyCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Optional<Currency> findByCode(CurrencyCode code);
}
