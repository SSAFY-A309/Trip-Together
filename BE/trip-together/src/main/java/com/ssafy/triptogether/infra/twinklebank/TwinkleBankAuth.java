package com.ssafy.triptogether.infra.twinklebank;

import java.util.Map;

import com.ssafy.triptogether.infra.twinklebank.data.request.TwinkleTokenRequest;

public interface TwinkleBankAuth {
	Map<String, String> getTwinkleBankToken(TwinkleTokenRequest twinkleTokenRequest, String code);

}
