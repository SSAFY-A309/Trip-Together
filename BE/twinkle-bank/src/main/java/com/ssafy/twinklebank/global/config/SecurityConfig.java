package com.ssafy.twinklebank.global.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ssafy.twinklebank.auth.filter.JwtAuthenticationFilter;
import com.ssafy.twinklebank.auth.provider.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
	private final JwtTokenProvider jwtTokenProvider;
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.httpBasic(HttpBasicConfigurer::disable) // http 기본 인증 비활성화
			.csrf(CsrfConfigurer::disable) // csrf 보호 비활성화
			.cors(cors -> cors.configurationSource(corsConfigurationSource())) // cors 설정 커스텀
			.sessionManagement(configurer ->
				configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 관리 정책 설정 : STATELESS
			.authorizeHttpRequests(authorize ->
				authorize
					.requestMatchers("/member/v1/oauth/authorize", "/member/v1/oauth/token").permitAll() // code 발급시에는 jwt filter를 타지 않음
					.anyRequest().authenticated())
			.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
				UsernamePasswordAuthenticationFilter.class); // jwt 인증필터 추가
		// .addFilterBefore(new ExceptionHandlerFilter(), JwtAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(
			List.of("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*")); // 모든 헤더 허용
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
