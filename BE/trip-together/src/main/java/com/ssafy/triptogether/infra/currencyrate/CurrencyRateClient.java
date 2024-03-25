package com.ssafy.triptogether.infra.currencyrate;

import com.ssafy.triptogether.infra.currencyrate.data.response.CurrencyRateResponse;

import java.util.List;

public interface CurrencyRateClient {
    List<CurrencyRateResponse> currencyRatesLoad();
}
