package com.ssafy.triptogether.auth.service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.triptogether.auth.data.response.TokenInfo;
import com.ssafy.triptogether.auth.data.response.TokenResponse;
import com.ssafy.triptogether.auth.provider.JwtTokenProvider;
import com.ssafy.triptogether.infra.twinklebank.TwinkleBankAuth;
import com.ssafy.triptogether.infra.twinklebank.TwinkleBankClient;
import com.ssafy.triptogether.infra.twinklebank.data.request.TwinkleTokenRequest;
import com.ssafy.triptogether.infra.twinklebank.data.response.TwinkleMemberInfoResponse;
import com.ssafy.triptogether.member.domain.Member;
import com.ssafy.triptogether.member.repository.MemberRepository;
import com.ssafy.triptogether.member.service.MemberSaveService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class AuthServiceImpl implements AuthLoadService, AuthSaveService {
	private final TwinkleBankAuth twinkleBankAuth;
	private final TwinkleBankClient twinkleBankClient;
	private final MemberSaveService memberSaveService;
	private final JwtTokenProvider jwtTokenProvider;
	private final StringRedisTemplate redisTemplate;
	private final MemberRepository memberRepository;

	private final long TWINKLE_ACCESS_TOKEN_EXPIRE_TIME = 6 * 60 * 1000L; // 6분
	private final long TWINKLE_REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L; // 8일

	@Transactional
	public TokenResponse getTripToken(String code, String SECRET_KEY, String CLIENT_ID) {
		// 	여행 → 은행으로 은행 access token  발급 요청
		// : 쿼리 파라미터로 `code` , `client_id`, `redirect_url` 을 전달하고, request body에 `secret_key`를 담아서 요청을 보낸다.
		Map<String, String> twinkleBankTokenMap = twinkleBankAuth.getTwinkleBankToken(
			new TwinkleTokenRequest(SECRET_KEY), code);

		// 여행 서버는 발급 받은 은행 서버의 토큰을 사용하여 은행 사용자 정보를 조회
		TwinkleMemberInfoResponse twinkleMemberInfoResponse = twinkleBankClient.bankMemberInfoLoad(CLIENT_ID,
			twinkleBankTokenMap.get("access"));

		// 조회해온 사용자 정보가 이미 있으면 저장하지 않고, 없으면 새롭게 저장
		Member member = memberRepository.findMemberByUuid(twinkleMemberInfoResponse.memberUuid())
			.orElseGet(() -> memberSaveService.saveMember(twinkleMemberInfoResponse));

		// 여행 서버의 access token(여행 사용자 pk/created/expire), refresh token(여행 사용자 pk)을 발급한다.
		Authentication authentication =
			new UsernamePasswordAuthenticationToken(member.getId(), member.getUuid(),
				Collections.singleton(new SimpleGrantedAuthority("AUTHORITY")));

		Map<String, String> tripTokenMap = jwtTokenProvider.generateToken(member.getId(), member.getUuid(),
			authentication);

		// accessToken 까서 created at, expires in 빼서 넣어주기
		Claims claims = jwtTokenProvider.parseClaims(tripTokenMap.get("access").substring(7));
		System.out.println(claims.get("created").getClass() + " " + claims.get("expiresIn").getClass());

		Long createdAt = (Long)claims.get("created");
		Integer expiresIn = (Integer)claims.get("expiresIn");
		Boolean isPin = member.getPinNum() != null;

		System.out.println(createdAt + " " + expiresIn);

		TokenInfo tokenInfo = TokenInfo.builder()
			.accessToken(tripTokenMap.get("access"))
			.createdAt(createdAt)
			.expiresIn(expiresIn)
			.build();

		TokenResponse response = TokenResponse.builder()
			.memberId(member.getId())
			.username(member.getUsername())
			.nickname(member.getNickname())
			.imageUrl(member.getImageUrl())
			.description(member.getDescription())
			.isPin(isPin)
			.tokenInfo(tokenInfo)
			.build();

		// 사용자 uuid로 은행 access token, refresh token을 저장한다.
		saveToken("access:" + member.getUuid(), twinkleBankTokenMap.get("access"), TWINKLE_ACCESS_TOKEN_EXPIRE_TIME);
		saveToken("refresh:" + member.getUuid(), twinkleBankTokenMap.get("refresh"), TWINKLE_REFRESH_TOKEN_EXPIRE_TIME);

		// 사용자의 pk로 여행 refresh token을 저장한다.
		saveToken("refresh:" + member.getId(), tripTokenMap.get("refresh"),
			jwtTokenProvider.getREFRESH_TOKEN_EXPIRE_TIME());

		return response;
	}

	private void saveToken(String key, String value, long expire) {
		redisTemplate.opsForValue()
			.set(key, value, expire, TimeUnit.MILLISECONDS);
	}
}
