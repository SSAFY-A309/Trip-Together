package com.ssafy.triptogether.infra.twinklebank;

import static com.ssafy.triptogether.global.exception.response.ErrorCode.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ssafy.triptogether.global.data.response.ApiResponse;
import com.ssafy.triptogether.global.exception.exceptions.category.ExternalServerException;
import com.ssafy.triptogether.global.exception.exceptions.category.NotFoundException;
import com.ssafy.triptogether.infra.twinklebank.data.request.TwinkleTokenRequest;
import com.ssafy.triptogether.infra.twinklebank.data.response.TwinkleTokenResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TwinkleBankAuthImpl implements TwinkleBankAuth {
	private final RestTemplate restTemplate;
	private final StringRedisTemplate redisTemplate;
	static final String TWINKLE_BANK_URI = "https://www.twinklebank.go.kr";

	@Override
	public Map<String, String> getTwinkleBankToken(TwinkleTokenRequest twinkleTokenRequest, String code) {
		// 쿼리 파라미터로 code , client_id, redirect_url 을 전달하고, request body에 secret_key를 담아서 요청을 보낸다.
		String url = UriComponentsBuilder.fromHttpUrl(TWINKLE_BANK_URI + "/api/member/v1/oauth/token")
			.queryParam("code", code)
			.queryParam("client_id", "123456")
			.queryParam("redirect_url", "redirecturl")
			.toUriString();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<TwinkleTokenRequest> entity = new HttpEntity<>(twinkleTokenRequest, headers);

		ResponseEntity<ApiResponse> response = restTemplate.exchange(
			url,
			HttpMethod.POST,
			entity,
			ApiResponse.class
		);

		if (response.getStatusCode() == HttpStatus.OK) {
			// accessToken 꺼내기
			String accessToken = getAccessToken(response);
			// refreshToken 꺼내기
			String refreshToken = getRefreshToken(response);

			if (accessToken == null || accessToken.isEmpty()){
				throw new NotFoundException("getTwinkleBankToken", UNDEFINED_ACCESS_TOKEN);
			}
			if (refreshToken == null || refreshToken.isEmpty()){
				throw new NotFoundException("getTwinkleBankToken", UNDEFINED_REFRESH_TOKEN);
			}

			Map<String, String> tokenMap = new HashMap<>();
			tokenMap.put("access", accessToken);
			tokenMap.put("refresh", refreshToken);

			return tokenMap;
		}

		throw new ExternalServerException("getTwinkleBankToken", TWINKLE_BANK_SERVER_ERROR);
	}

	private static String getAccessToken(ResponseEntity<ApiResponse> response) {
		TwinkleTokenResponse res = (TwinkleTokenResponse)response.getBody().getData();
		String accessToken = res.accessToken();
		return accessToken;
	}

	private static String getRefreshToken(ResponseEntity<ApiResponse> response) {
		String refreshToken = "";
		List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
		if (cookies != null) {
			for (String cookie : cookies) {
				if (cookie.startsWith("refreshToken=")) {
					refreshToken = cookie.split(";")[0].split("=")[1];
					System.out.println("Refresh Token: " + refreshToken);
				}
			}
		}
		return refreshToken;
	}
}
