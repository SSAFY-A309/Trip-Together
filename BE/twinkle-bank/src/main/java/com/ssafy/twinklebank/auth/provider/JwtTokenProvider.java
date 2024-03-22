package com.ssafy.twinklebank.auth.provider;

import static com.ssafy.twinklebank.global.exception.response.ErrorCode.*;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	@Getter
	private final long ACCESS_TOKEN_EXPIRE_TIME = 5 * 60 * 1000L; // 5분
	@Getter
	private final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L; // 7일

	// security Key
	private final Key key;
	private UserDetailsServiceImpl userDetailsService;

	public JwtTokenProvider(@Value("${jwt.secret}") String key) {
		byte[] keyBytes = Decoders.BASE64.decode(key);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	// Member 정보를 가지고 AccessToken, RefreshToken을 생성
	public Map<String, String> generateToken(long id, String uuid, Authentication authentication) {
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
		String accessToken = createToken(now, ACCESS_TOKEN_EXPIRE_TIME, Jwts.builder()
			.setSubject(authentication.getName())
			.claim("auth", authorities)
			.claim("uuid", uuid), id);

		// refresh token 발급
		String refreshToken = createToken(now, REFRESH_TOKEN_EXPIRE_TIME, Jwts.builder(), id);

		HashMap<String, String> map = new HashMap<>();
		map.put("access", SecurityUtil.getTokenPrefix() + " " + accessToken);
		map.put("refresh", refreshToken); // Bearer을 붙이지 않음
		return map;
	}

	private String createToken(long now, long EXPIRE_TIME, JwtBuilder authentication, long id) {
		Date accessTokenExpireIn = new Date(now + EXPIRE_TIME);
		return authentication
			.claim("id", id)
			.setExpiration(accessTokenExpireIn)
			.signWith(key, SignatureAlgorithm.HS256)
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
