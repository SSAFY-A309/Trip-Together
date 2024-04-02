package com.ssafy.triptogether.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	@Value("${app.bankUrl}")
	private String TWINKLE_BANK_URI;
	@Bean
	public WebClient webClient(){
		return WebClient.builder()
			.baseUrl(TWINKLE_BANK_URI)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.build();
	}
}
