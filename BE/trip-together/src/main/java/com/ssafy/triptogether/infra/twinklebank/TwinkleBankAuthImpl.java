package com.ssafy.triptogether.infra.twinklebank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.triptogether.global.data.response.ApiResponse;
import com.ssafy.triptogether.global.exception.exceptions.category.ExternalServerException;
import com.ssafy.triptogether.global.exception.exceptions.category.NotFoundException;
import com.ssafy.triptogether.global.exception.exceptions.category.UnAuthorizedException;
import com.ssafy.triptogether.infra.twinklebank.data.request.TwinkleBankTransfer1wonRequest;
import com.ssafy.triptogether.infra.twinklebank.data.request.TwinkleBankVerify1wonRequest;
import com.ssafy.triptogether.infra.twinklebank.data.request.TwinkleTokenRequest;
import com.ssafy.triptogether.infra.twinklebank.data.response.TwinkleTokenResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.ssafy.triptogether.global.exception.response.ErrorCode.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class TwinkleBankAuthImpl implements TwinkleBankAuth {
	private final RestTemplate restTemplate;
	private final WebClient webClient;
	private final StringRedisTemplate redisTemplate;
	private final ObjectMapper objectMapper;

	@Value("${app.bankUrl}")
	private String TWINKLE_BANK_URI;

	@Value("${app.redirectUrl}")
	private String TWINKLE_REDIRECT_URL;

	@Value("${app.clientId}")
	private String TWINKLE_CLIENT_ID;

	private final long TWINKLE_ACCESS_TOKEN_EXPIRE_TIME = 30 * 24 * 60 * 60 * 1000L; // 6분
	private final long TWINKLE_REFRESH_TOKEN_EXPIRE_TIME = 30 * 24 * 60 * 60 * 1000L; // 8일

	@Override
	public Map<String, String> getTwinkleBankToken(TwinkleTokenRequest twinkleTokenRequest, String code) {
		// 쿼리 파라미터로 code , client_id, redirect_url 을 전달하고, request body에 secret_key를 담아서 요청을 보낸다.
		String url = UriComponentsBuilder.fromHttpUrl(TWINKLE_BANK_URI + "/member/v1/oauth/token")
			.queryParam("code", code)
			.queryParam("client_id", TWINKLE_CLIENT_ID)
			.queryParam("redirect_url", TWINKLE_REDIRECT_URL)
			.toUriString();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<TwinkleTokenRequest> entity = new HttpEntity<>(twinkleTokenRequest, headers);

		try {
			ResponseEntity<ApiResponse> response = restTemplate.exchange(
				url,
				HttpMethod.POST,
				entity,
				ApiResponse.class
			);
			// accessToken 꺼내기
			String accessToken = getAccessToken(response);
			// refreshToken 꺼내기
			String refreshToken = getRefreshToken(response);

			if (accessToken == null || accessToken.isEmpty()) {
				throw new NotFoundException("getTwinkleBankToken", UNDEFINED_ACCESS_TOKEN);
			}
			if (refreshToken == null || refreshToken.isEmpty()) {
				throw new NotFoundException("getTwinkleBankToken", UNDEFINED_REFRESH_TOKEN);
			}

			Map<String, String> tokenMap = new HashMap<>();
			tokenMap.put("access", accessToken);
			tokenMap.put("refresh", refreshToken);

			return tokenMap;
		} catch (RestClientException e) {
			throw new ExternalServerException("getTwinkleBankToken", TWINKLE_BANK_SERVER_ERROR);
		}
	}

	@Override
	public void transfer1won(TwinkleBankTransfer1wonRequest twinkleBankTransfer1wonRequest, String memberUuid) {
		// String url = UriComponentsBuilder.fromHttpUrl(TWINKLE_BANK_URI + "/account/v1/accounts/1wontransfer")
		// 	.toUriString();
		String accessToken = redisTemplate.opsForValue().get("access:" + memberUuid);

		// TODO : bank access token이 만료되었거나, 발급받지 않았을 경우 예외 상황 처리
		accessToken = reissueIfExpired(memberUuid, accessToken);

		// HttpHeaders headers = new HttpHeaders();
		// headers.set("Authorization", accessToken);
		// headers.setContentType(MediaType.APPLICATION_JSON);
		// HttpEntity<TwinkleBankTransfer1wonRequest> entity = new HttpEntity<>(twinkleBankTransfer1wonRequest, headers);
		// log.debug("transfer1won bank accesstoken " + headers.get("Authorization"));

		try{
			ApiResponse response = webClient
				.post()
				.uri("/account/v1/accounts/1wontransfer")
				.header("Authorization", accessToken)
				.bodyValue(twinkleBankTransfer1wonRequest)
				.retrieve()
				.bodyToMono(ApiResponse.class)
				.block();

		} catch (RestClientException e) {
			throw new ExternalServerException("transfer1won", TWINKLE_BANK_SERVER_ERROR);
		}

		// try {
		// 	ResponseEntity<ApiResponse> response = restTemplate.exchange(
		// 		url,
		// 		HttpMethod.POST,
		// 		entity,
		// 		ApiResponse.class
		// 	);
		// } catch (RestClientException e) {
		// 	throw new ExternalServerException("transfer1won", TWINKLE_BANK_SERVER_ERROR);
		// }
	}

	@Override
	public void verify1won(TwinkleBankVerify1wonRequest twinkleBankVerify1wonRequest, String memberUuid) {
		String url = UriComponentsBuilder.fromHttpUrl(TWINKLE_BANK_URI + "/account/v1/accounts/1wonverify")
			.toUriString();
		String accessToken = redisTemplate.opsForValue().get("access:" + memberUuid);

		// TODO : bank access token이 만료되었거나, 발급받지 않았을 경우 예외 상황 처리
		accessToken = reissueIfExpired(memberUuid, accessToken);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", accessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<TwinkleBankVerify1wonRequest> entity = new HttpEntity<>(twinkleBankVerify1wonRequest, headers);

		try {
			ResponseEntity<ApiResponse> response = restTemplate.exchange(
				url,
				HttpMethod.POST,
				entity,
				ApiResponse.class
			);
		} catch (RestClientException e) {
			throw new ExternalServerException("verify1won", TWINKLE_BANK_SERVER_ERROR);
		}
	}

	private String reissueIfExpired(String memberUuid, String accessToken) {
		if (accessToken == null){ // 만료되었거나, 발급받지 않았거나
			if (!redisTemplate.opsForValue().get("refresh:" + memberUuid).isEmpty()){ // refresh는 있는 경우 - 만료
				reissueBank(memberUuid);
				accessToken = redisTemplate.opsForValue().get("access:" + memberUuid);
			}else{ // 발급받지 않았을 경우 -> 다시 은행 토큰 받도록
				throw new UnAuthorizedException("twinklebank authimpl : 은행 재로그인 필요 ", UNAUTHORIZED_BANK);
			}
		}
		return accessToken;
	}

	@Override
	public void reissueBank(String memberUuid) {
		String url = UriComponentsBuilder.fromHttpUrl(TWINKLE_BANK_URI + "/member/v1/members/reissue")
			.queryParam("client_id", TWINKLE_CLIENT_ID)
			.toUriString();
		String refreshToken = redisTemplate.opsForValue().get("refresh:" + memberUuid);

		HttpHeaders headers = new HttpHeaders();
		headers.set("cookie", "refreshToken="+ refreshToken);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		try{
			ResponseEntity<ApiResponse> response = restTemplate.exchange(
				url,
				HttpMethod.GET,
				entity,
				ApiResponse.class
			);

			// accessToken 꺼내기
			String newAccessToken = getAccessToken(response);
			// refreshToken 꺼내기
			String newRefreshToken = getRefreshToken(response);

			if (newAccessToken == null || newAccessToken.isEmpty()) {
				throw new NotFoundException("getTwinkleBankToken", UNDEFINED_ACCESS_TOKEN);
			}
			if (newRefreshToken == null || newRefreshToken.isEmpty()) {
				throw new NotFoundException("getTwinkleBankToken", UNDEFINED_REFRESH_TOKEN);
			}
			// 다시 저장
			saveToken("access:" + memberUuid, newAccessToken, TWINKLE_ACCESS_TOKEN_EXPIRE_TIME);
			saveToken("refresh:" + memberUuid, newRefreshToken, TWINKLE_REFRESH_TOKEN_EXPIRE_TIME);

		} catch (RestClientException e) {
			throw new ExternalServerException("TwinkleBankAccountsLoad", TWINKLE_BANK_SERVER_ERROR);
		}
	}

	private String getAccessToken(ResponseEntity<ApiResponse> response) {
		System.out.println(response.getBody().getData());
		TwinkleTokenResponse res = objectMapper.convertValue(response.getBody().getData(), TwinkleTokenResponse.class);
		return res.accessToken();
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

	private void saveToken(String key, String value, long expire) {
		redisTemplate.opsForValue()
			.set(key, value, expire, TimeUnit.MILLISECONDS);
	}
}
