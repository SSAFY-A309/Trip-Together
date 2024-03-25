package com.ssafy.triptogether.infra.currencyrate;

import com.ssafy.triptogether.global.exception.exceptions.category.ExternalServerException;
import com.ssafy.triptogether.global.exception.response.ErrorCode;
import com.ssafy.triptogether.infra.currencyrate.data.response.CurrencyRateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CurrencyRateClientImpl implements CurrencyRateClient {
    static final String CURRENCY_RATE_API_URL = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON";
    static final String data = "AP01";
    private final RestTemplate restTemplate;
    @Value("${EXCHANGE_RATE_API_KEY}")
    private String authKey;

    /**
     * 환율 정보 조회
     *
     * @return 환율 전체 정보
     */
    @Override
    public List<CurrencyRateResponse> currencyRatesLoad() {
        String url = UriComponentsBuilder.fromHttpUrl(CURRENCY_RATE_API_URL)
                .queryParam("authkey", authKey)
                .queryParam("data", data)
                .toUriString();

        ResponseEntity<List<CurrencyRateResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }

        throw new ExternalServerException("CurrencyRatesLoad", ErrorCode.CURRENCY_RATE_LOAD_SERVER_ERROR);
    }
}
