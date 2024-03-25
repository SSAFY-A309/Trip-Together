package com.ssafy.triptogether.tripaccount.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import com.ssafy.triptogether.global.exception.exceptions.category.NotFoundException;
import com.ssafy.triptogether.member.domain.Gender;
import com.ssafy.triptogether.member.domain.Member;
import com.ssafy.triptogether.tripaccount.data.response.AccountHistoriesLoadDetail;
import com.ssafy.triptogether.tripaccount.data.response.CurrenciesLoadResponse;
import com.ssafy.triptogether.tripaccount.data.response.RateLoadResponse;
import com.ssafy.triptogether.tripaccount.data.response.TripAccountsLoadDetail;
import com.ssafy.triptogether.tripaccount.data.response.TripAccountsLoadResponse;
import com.ssafy.triptogether.tripaccount.domain.AccountHistory;
import com.ssafy.triptogether.tripaccount.domain.Currency;
import com.ssafy.triptogether.tripaccount.domain.CurrencyCode;
import com.ssafy.triptogether.tripaccount.domain.CurrencyNation;
import com.ssafy.triptogether.tripaccount.domain.TripAccount;
import com.ssafy.triptogether.tripaccount.domain.Type;
import com.ssafy.triptogether.tripaccount.repository.AccountHistoryRepository;
import com.ssafy.triptogether.tripaccount.repository.CurrencyRepository;
import com.ssafy.triptogether.tripaccount.repository.TripAccountRepository;

@ExtendWith(MockitoExtension.class)
class TripAccountServiceImplTest {
	@InjectMocks
	TripAccountServiceImpl tripAccountService;
	@Mock
	CurrencyRepository currencyRepository;
	@Mock
	TripAccountRepository tripAccountRepository;
	@Mock
	AccountHistoryRepository accountHistoryRepository;

    @Nested
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("환전 가능 통화 목록 조회")
    class CurrenciesLoadTest {
        List<Currency> testCurrencies;

        @BeforeEach
        void setUp() {
            Currency currency1 = Currency.builder()
                    .code(CurrencyCode.EUR)
                    .currencyNation(CurrencyNation.EUR)
                    .rate(365.1)
                    .build();
            Currency currency2 = Currency.builder()
                    .code(CurrencyCode.GBP)
                    .currencyNation(CurrencyNation.UK)
                    .rate(255.5)
                    .build();

            testCurrencies = Arrays.asList(currency1, currency2);
            // given
            given(currencyRepository.findAll()).willReturn(testCurrencies);
        }

		@Test
		@DisplayName("전체 통화 목록 조회 성공")
		void currenciesLoadSuccess() {
			// when
			CurrenciesLoadResponse response = tripAccountService.currenciesLoad();
			// then
			assertAll(
				() -> assertNotNull(response, "응답은 null 이 아니어야 합니다."),
				() -> assertEquals(2, response.currenciesLoadDetail().size(), "통화 목록의 크기가 예상과 다릅니다."),
				() -> assertEquals(CurrencyCode.EUR, response.currenciesLoadDetail().get(0).code(),
					"첫 번째 통화 코드가 예상과 다릅니다."),
				() -> assertEquals("유럽", response.currenciesLoadDetail().get(0).nationKr(),
					"첫 번째 통화의 한국어 국가명이 예상과 다릅니다.")
			);
		}

        @Test
        @DisplayName("통화 환율 조회 실패")
        void currencyRateLoadFail() {
            // given
            given(currencyRepository.findByCode(CurrencyCode.fromString("USD"))).willReturn(Optional.empty());
            // when & then
            assertThrows(NotFoundException.class, () -> {
                tripAccountService.rateLoad(CurrencyCode.fromString("USD"));
            });
        }

		@Test
		@DisplayName("통화 환율 조회 성공")
		void currencyRateLoadSuccess() {
			// given
			given(currencyRepository.findByCode(CurrencyCode.GBP)).willReturn(
				Optional.ofNullable(testCurrencies.get(1)));
			// when
			RateLoadResponse rateLoadResponse = tripAccountService.rateLoad(CurrencyCode.GBP);
			// then
			assertEquals(testCurrencies.get(1).getRate(), rateLoadResponse.rate());
		}
	}

	@Nested
	@DisplayName("지갑 내 목록 조회")
	class TripAccountsLoadTest {
		long memberId = 1L;
		List<TripAccount> tripAccounts;
		TripAccountsLoadResponse testTripAccountsLoadResponse;

		@BeforeEach
		void setUp() {
			Member member = Member.builder()
				.gender(Gender.MALE)
				.nickname("TestUser")
				.uuid("TestUser")
				.birth(LocalDate.now())
				.build();
			Currency currency1 = Currency.builder()
				.code(CurrencyCode.EUR)
				.currencyNation(CurrencyNation.EUR)
				.rate(365.1)
				.build();
			Currency currency2 = Currency.builder()
				.code(CurrencyCode.GBP)
				.currencyNation(CurrencyNation.UK)
				.rate(255.5)
				.build();
			TripAccount tripAccount1 = TripAccount.builder()
				.balance(3.0)
				.currency(currency1)
				.member(member)
				.build();
			TripAccount tripAccount2 = TripAccount.builder()
				.balance(10.0)
				.currency(currency2)
				.member(member)
				.build();
			tripAccounts = List.of(tripAccount1, tripAccount2);
			TripAccountsLoadDetail tripAccountsLoadDetail1 = TripAccountsLoadDetail.builder()
				.currencyNation(CurrencyNation.EUR)
				.nationKr(CurrencyNation.EUR.getMessage())
				.balance(3.0)
				.unit(CurrencyCode.EUR.getUnit())
				.build();
			TripAccountsLoadDetail tripAccountsLoadDetail2 = TripAccountsLoadDetail.builder()
				.currencyNation(CurrencyNation.UK)
				.nationKr(CurrencyNation.UK.getMessage())
				.balance(10.0)
				.unit(CurrencyCode.GBP.getUnit())
				.build();
			testTripAccountsLoadResponse = TripAccountsLoadResponse.builder()
				.tripAccountsLoadDetails(List.of(tripAccountsLoadDetail1, tripAccountsLoadDetail2))
				.tripAccountCount(2)
				.build();
		}

		@Test
		void tripAccountsLoad() {
			// given
			given(tripAccountRepository.findByMemberId(memberId)).willReturn(tripAccounts);
			// when
			TripAccountsLoadResponse tripAccountsLoadResponse = tripAccountService.tripAccountsLoad(memberId);
			// then
			assertAll(
				() -> assertEquals(testTripAccountsLoadResponse.tripAccountsLoadDetails(),
					tripAccountsLoadResponse.tripAccountsLoadDetails()),
				() -> assertEquals(testTripAccountsLoadResponse.tripAccountCount(),
					tripAccountsLoadResponse.tripAccountCount())
			);
			verify(tripAccountRepository, times(1)).findByMemberId(memberId);
		}
	}

	@Nested
	@DisplayName("전체 통화 거래 내역 조회")
	class AccountHistoriesLoadTest {
		long memberId = 1L;
		Pageable pageable;
		Page<AccountHistory> testAccountHistories;

		@BeforeEach
		void setUp() {
			pageable = Pageable.unpaged();
			Member member = Member.builder()
				.gender(Gender.MALE)
				.nickname("TestUser")
				.uuid("TestUser")
				.birth(LocalDate.now())
				.build();
			Currency currency = Currency.builder()
				.code(CurrencyCode.EUR)
				.currencyNation(CurrencyNation.EUR)
				.rate(1.0)
				.build();
			TripAccount tripAccount = TripAccount.builder()
				.member(member)
				.currency(currency)
				.balance(100.0)
				.build();
			AccountHistory accountHistory = AccountHistory.builder()
				.type(Type.DEPOSIT)
				.businessName("Test Business")
				.quantity(50.0)
				.tripAccount(tripAccount)
				.build();
			List<AccountHistory> accountHistoriesList = new ArrayList<>();
			accountHistoriesList.add(accountHistory);
			testAccountHistories = new PageImpl<>(accountHistoriesList, pageable, 1);
		}

		@Test
		void accountHistoriesLoad() {
			// given
			given(accountHistoryRepository.findAccountHistoriesLoadDetailByMemberId(anyLong(),
				any(Pageable.class))).willReturn(testAccountHistories);
			// when
			Page<AccountHistoriesLoadDetail> resultPage = tripAccountService.accountHistoriesLoad(memberId, pageable);
			// then
			assertEquals(1, resultPage.getContent().size());
			AccountHistoriesLoadDetail result = resultPage.getContent().get(0);
			assertEquals("유럽", result.nationKr());
			assertEquals(CurrencyCode.EUR.getUnit(), result.unit());
			assertEquals("충전", result.type());
			assertEquals("Test Business", result.usage());
			assertEquals(50.0, result.quantity());
		}
	}
}