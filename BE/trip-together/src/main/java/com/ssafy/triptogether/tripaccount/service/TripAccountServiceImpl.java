package com.ssafy.triptogether.tripaccount.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.triptogether.auth.data.request.PinVerifyRequest;
import com.ssafy.triptogether.auth.validator.pin.PinVerify;
import com.ssafy.triptogether.global.exception.exceptions.category.NotFoundException;
import com.ssafy.triptogether.global.exception.response.ErrorCode;
import com.ssafy.triptogether.infra.currencyrate.CurrencyRateClient;
import com.ssafy.triptogether.infra.currencyrate.data.response.CurrencyRateResponse;
import com.ssafy.triptogether.infra.twinklebank.TwinkleBankClient;
import com.ssafy.triptogether.infra.twinklebank.data.request.TwinkleBankAccountExchangeRequest;
import com.ssafy.triptogether.member.domain.Member;
import com.ssafy.triptogether.member.repository.MemberRepository;
import com.ssafy.triptogether.member.utils.MemberUtils;
import com.ssafy.triptogether.tripaccount.concurrency.DistributedLock;
import com.ssafy.triptogether.tripaccount.data.request.TripAccountExchangeRequest;
import com.ssafy.triptogether.tripaccount.data.response.AccountHistoriesLoadDetail;
import com.ssafy.triptogether.tripaccount.data.response.CurrenciesLoadDetail;
import com.ssafy.triptogether.tripaccount.data.response.CurrenciesLoadResponse;
import com.ssafy.triptogether.tripaccount.data.response.RateLoadResponse;
import com.ssafy.triptogether.tripaccount.data.response.TripAccountsLoadDetail;
import com.ssafy.triptogether.tripaccount.data.response.TripAccountsLoadResponse;
import com.ssafy.triptogether.tripaccount.domain.AccountHistory;
import com.ssafy.triptogether.tripaccount.domain.Currency;
import com.ssafy.triptogether.tripaccount.domain.CurrencyCode;
import com.ssafy.triptogether.tripaccount.domain.TripAccount;
import com.ssafy.triptogether.tripaccount.repository.AccountHistoryRepository;
import com.ssafy.triptogether.tripaccount.repository.CurrencyRepository;
import com.ssafy.triptogether.tripaccount.repository.TripAccountRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TripAccountServiceImpl implements TripAccountLoadService, TripAccountSaveService {
	// Repository
	private final CurrencyRepository currencyRepository;
	private final TripAccountRepository tripAccountRepository;
	private final AccountHistoryRepository accountHistoryRepository;
	private final MemberRepository memberRepository;
	// Client
	private final CurrencyRateClient currencyRateClient;
	private final TwinkleBankClient twinkleBankClient;

    /**
     * 환전 가능 통화 목록을 조회하는 메서드
     *
     * @return 환전 가능 통화 목록
     */
    @Override
    public CurrenciesLoadResponse currenciesLoad() {
        List<Currency> currencies = currencyRepository.findAll();
        List<CurrenciesLoadDetail> collectCurrencies = currencies.stream()
                .map(
                        currency -> CurrenciesLoadDetail.builder()
                                .code(currency.getCode())
                                .nation(currency.getCurrencyNation())
                                .nationKr(currency.getCurrencyNation().getMessage())
                                .unit(currency.getCode().getUnit())
                                .build()
                ).toList();
        return CurrenciesLoadResponse.builder()
                .currenciesLoadDetail(collectCurrencies)
                .build();
    }

    /**
     * 해당 통화 코드의 환율 정보 반환
     *
     * @param currencyCode 요청 통화 코드
     * @return 환율 정보
     */
    @Override
    public RateLoadResponse rateLoad(CurrencyCode currencyCode) {
        Currency currency = currencyRepository.findByCode(currencyCode)
                .orElseThrow(
                        () -> new NotFoundException("RateLoad", ErrorCode.CURRENCY_NOT_FOUND, currencyCode)
                );
        return RateLoadResponse.builder()
                .rate(currency.getRate())
                .build();
    }

	/**
	 * 회원의 지갑 내 목록 조회
	 * @param memberId 요청자의 member_id
	 * @return 지갑 내 목록
	 */
	@Override
	public TripAccountsLoadResponse tripAccountsLoad(long memberId) {
		List<TripAccount> tripAccounts = tripAccountRepository.findByMemberId(memberId);
		List<TripAccountsLoadDetail> tripAccountsLoadDetails = tripAccounts.stream()
			.map(tripAccount -> TripAccountsLoadDetail.builder()
				.currencyNation(tripAccount.getCurrency().getCurrencyNation())
				.nationKr(tripAccount.getCurrency().getCurrencyNation().getMessage())
				.balance(tripAccount.getBalance())
				.unit(tripAccount.getCurrency().getCode().getUnit())
				.build()
			).toList();
		return TripAccountsLoadResponse.builder()
			.tripAccountsLoadDetails(tripAccountsLoadDetails)
			.tripAccountCount(tripAccountsLoadDetails.size())
			.build();
	}

	/**
	 * 지갑 전체 거래 내역 조회
	 * @param memberId 요청자의 member_id
	 * @param pageable 페이징 기준
	 * @return 페이징에 따른 거래 내역
	 */
	@Override
	public Page<AccountHistoriesLoadDetail> accountHistoriesLoad(long memberId, Pageable pageable) {
		Page<AccountHistory> accountHistories = accountHistoryRepository.findAccountHistoriesLoadDetailByMemberId(
			memberId, pageable);

		List<AccountHistoriesLoadDetail> accountHistoriesLoadDetails = accountHistories.getContent().stream()
			.map(accountHistory -> AccountHistoriesLoadDetail.builder()
				.currencyNation(accountHistory.getTripAccount().getCurrency().getCurrencyNation())
				.nationKr(accountHistory.getTripAccount().getCurrency().getCurrencyNation().getMessage())
				.unit(accountHistory.getTripAccount().getCurrency().getCode().getUnit())
				.type(accountHistory.getType().getMessage())
				.usage(accountHistory.getBusinessName())
				.quantity(accountHistory.getQuantity())
				.createdAt(accountHistory.getCreatedAt())
				.build()
			).toList();

		return new PageImpl<>(accountHistoriesLoadDetails, pageable, accountHistories.getTotalElements());
	}

	/**
	 * 전체 통화 코드의 환율 정보 업데이트
	 */
	@Transactional
	@Override
	public void currencyRateUpdate() {
		List<CurrencyRateResponse> currencyRateResponses = currencyRateClient.currencyRatesLoad();
		Map<CurrencyCode, Currency> currencyMap = currencyRepository.findAll()
			.stream()
			.collect(Collectors.toMap(Currency::getCode, Function.identity()));
		currencyRateResponses.forEach(currencyRateResponse -> {
			CurrencyCode currencyCode = CurrencyCode.fromString(currencyRateResponse.cur_unit());
			Currency currency = currencyMap.get(currencyCode);
			if (currency != null) {
				currency.updateRate(Double.valueOf(currencyRateResponse.dealBasR()));
			}
		});
	}

	/**
	 * 환전 요청
	 * @param memberId 요청자의 member_id
	 * @param pinVerifyRequest PIN 인증 요청
	 * @param tripAccountExchangeRequest 환전 정보
	 */
	@PinVerify
	@DistributedLock(key = "'exchange:' + #memberId + ':' + #tripAccountExchangeRequest.fromCurrencyCode()")
	@Transactional
	@Override
	public void tripAccountExchange(long memberId, PinVerifyRequest pinVerifyRequest,
		TripAccountExchangeRequest tripAccountExchangeRequest) {
		Member member = MemberUtils.findByMemberId(memberRepository, memberId);

		if (tripAccountExchangeRequest.fromCurrencyCode().equals("KRW")) {
			Currency currency = getCurrency(tripAccountExchangeRequest.toCurrencyCode());
			tripAccountRepository.findByMemberIdAndCurrencyId(memberId, currency.getId())
				.ifPresent(tripAccount -> {
					tripAccount.depositBalance(tripAccountExchangeRequest.toQuantity());
				});
			TripAccount tripAccount = TripAccount.builder()
				.balance(tripAccountExchangeRequest.toQuantity())
				.currency(currency)
				.member(member)
				.build();
			tripAccountRepository.save(tripAccount);
			twinkleBankWithdrawRequest(tripAccountExchangeRequest);
			return;
		}

		Currency currency = getCurrency(tripAccountExchangeRequest.fromCurrencyCode());
		TripAccount tripAccount = tripAccountRepository.findByMemberIdAndCurrencyId(memberId, currency.getId())
			.orElseThrow(
				() -> new NotFoundException("TripAccountExchange", ErrorCode.TRIP_ACCOUNT_NOT_FOUND)
			);
		tripAccount.withdrawBalance(tripAccountExchangeRequest.fromQuantity());
		twinkleBankDepositRequest(tripAccountExchangeRequest);
	}

	private Currency getCurrency(String tripAccountExchangeRequest) {
		CurrencyCode currencyCode = CurrencyCode.fromString(tripAccountExchangeRequest);
		return currencyRepository.findByCode(currencyCode)
			.orElseThrow(
				() -> new NotFoundException("TripAccountExchange", ErrorCode.CURRENCY_NOT_FOUND)
			);
	}

	private void twinkleBankDepositRequest(TripAccountExchangeRequest tripAccountExchangeRequest) {
		TwinkleBankAccountExchangeRequest twinkleBankAccountExchangeRequest = TwinkleBankAccountExchangeRequest.builder()
			.accountUuid(tripAccountExchangeRequest.accountUuid())
			.price(tripAccountExchangeRequest.toQuantity())
			.type("deposit")
			.address("멀티 캠퍼스")
			.businessName("trip-together")
			.build();
		twinkleBankClient.bankAccountDeposit(twinkleBankAccountExchangeRequest);
	}

	private void twinkleBankWithdrawRequest(TripAccountExchangeRequest tripAccountExchangeRequest) {
		TwinkleBankAccountExchangeRequest twinkleBankAccountExchangeRequest = TwinkleBankAccountExchangeRequest.builder()
			.accountUuid(tripAccountExchangeRequest.accountUuid())
			.price(tripAccountExchangeRequest.fromQuantity())
			.type("withdraw")
			.address("멀티 캠퍼스")
			.businessName("trip-together")
			.build();
		twinkleBankClient.bankAccountWithdraw(twinkleBankAccountExchangeRequest);
	}
}
