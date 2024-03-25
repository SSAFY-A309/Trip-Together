package com.ssafy.triptogether.auth.controller;

import static com.ssafy.triptogether.global.data.response.StatusCode.*;
import static org.springframework.http.HttpStatus.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.triptogether.auth.data.response.TokenResponse;
import com.ssafy.triptogether.auth.service.AuthServiceImpl;
import com.ssafy.triptogether.global.data.response.ApiResponse;

@RestController
@RequestMapping("/member/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	// TODO : ID , REDIRECT_URL, SECRET_KEY 환경변수로 빼기
	private final static String CLIENT_ID = "123456"; // 은행의 ID
	private final static String REDIRECT_URL = "redirecturl"; // 등록 REDIRECT_URL
	private final static String SECRET_KEY = "secretkey"; // 은행 SECRET KEY
	private final AuthServiceImpl authService;

	@GetMapping("/token")
	public ResponseEntity<ApiResponse<TokenResponse>> getTripToken(@Valid @RequestParam(value = "code") String code) {
		TokenResponse response = authService.getTripToken(code, SECRET_KEY, CLIENT_ID);
		return ApiResponse.toResponseEntity(OK, SUCCESS_TRIP_TOKEN, response);
	}

}
