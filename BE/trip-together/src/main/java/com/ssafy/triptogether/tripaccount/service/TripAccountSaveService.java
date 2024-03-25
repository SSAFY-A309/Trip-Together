package com.ssafy.triptogether.tripaccount.service;

import com.ssafy.triptogether.auth.data.request.PinVerifyRequest;
import com.ssafy.triptogether.tripaccount.data.request.TripAccountExchangeRequest;

public interface TripAccountSaveService {
	void currencyRateUpdate();
	void tripAccountExchange(long memberId, PinVerifyRequest pinVerifyRequest, TripAccountExchangeRequest tripAccountExchangeRequest);
}
