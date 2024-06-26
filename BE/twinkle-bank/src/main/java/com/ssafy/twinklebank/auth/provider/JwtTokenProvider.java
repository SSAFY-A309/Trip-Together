package com.ssafy.twinklebank.auth.provider;

import static com.ssafy.twinklebank.global.exception.response.ErrorCode.*;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.ssafy.twinklebank.auth.service.UserDetailsServiceImpl;
import com.ssafy.twinklebank.auth.utils.SecurityMember;
import com.ssafy.twinklebank.auth.utils.SecurityUtil;
import com.ssafy.twinklebank.global.exception.exceptions.category.UnAuthorizedException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {
	// 은행의 access token의 유효 시간 : 5분
	// 은행의 refresh token의 유효 시간 : 7일
	// TODO : 은행 access token, refresh 유효시간 줄이기
	@Getter
	private final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 24 * 60 * 60 * 1000L; // 5분
	@Getter
	private final long REFRESH_TOKEN_EXPIRE_TIME = 30 * 24 * 60 * 60 * 1000L; // 7일

	// security Key
	private final Key key;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	public JwtTokenProvider(@Value("${jwt.secret}") String key) {
		byte[] keyBytes = Decoders.BASE64.decode(key);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	// Member 정보를 가지고 AccessToken, RefreshToken을 생성
	public Map<String, String> generateToken(Long id, String uuid, Authentication authentication) {
		SecurityMember securityMember = new SecurityMember(
			id,
			uuid,
			(String)authentication.getPrincipal(),
			(String)authentication.getCredentials()
		);

		String authorities = securityMember.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));

		// access token 발급
		long now = (new Date()).getTime();
		String accessToken = createToken(now, id, ACCESS_TOKEN_EXPIRE_TIME, Jwts.builder()
			.setSubject(authentication.getName())
			.claim("auth", authorities) // 넣고싶은 값 넣기
			.claim("uuid", uuid));

		// refresh token 발급
		String refreshToken = createToken(now, id, REFRESH_TOKEN_EXPIRE_TIME, Jwts.builder());

		HashMap<String, String> map = new HashMap<>();
		map.put("access", SecurityUtil.getTokenPrefix() + " " + accessToken);
		map.put("refresh", refreshToken); // Bearer을 붙이지 않음
		return map;
	}

	private String createToken(long now, Long id, long EXPIRE_TIME, JwtBuilder authentication) {
		Date tokenExpireIn = new Date(now + EXPIRE_TIME);
		return authentication
			.setExpiration(tokenExpireIn)
			.claim("id", id)
			.signWith(key, SignatureAlgorithm.HS256) // 원하는 방식
			.compact();
	}

	// access token을 복호하하여 토큰에 들어있는 정보를 꺼내는 메소드
	public Authentication getAuthentication(String accessToken) {
		Claims claims = parseClaims(accessToken);

		if (claims.get("auth") == null) {
			throw new UnAuthorizedException("JwtTokenProvider : ", UNAUTHORIZED_MEMBER);
		}

		// claims에서 name으로 securitymember을 가져온다
		String username = claims.getSubject();
		SecurityMember securityMember = (SecurityMember)userDetailsService.loadUserByUsername(username);

		List<SimpleGrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
			.map(SimpleGrantedAuthority::new)
			.toList();

		return new UsernamePasswordAuthenticationToken(securityMember, "", authorities);
	}

	// 토큰 정보를 검증하는 메서드
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT Token", e);
			throw e;
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT Token", e);
			throw e;
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT Token", e);
			throw e;
		} catch (IllegalArgumentException e) {
			log.info("JWT claims string is empty.", e);
		}
		return false;
	}

	public Claims parseClaims(String accessToken) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(accessToken)
				.getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}
}
