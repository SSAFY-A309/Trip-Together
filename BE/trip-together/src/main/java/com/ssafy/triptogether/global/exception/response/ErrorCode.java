package com.ssafy.triptogether.global.exception.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	// MEMBER
	UNDEFINED_MEMBER(404, "사용자 정보를 찾을 수 없습니다."),
	PIN_CHECK_MISS_MATCH(400, "핀번호와 핀번호 확인이 서로 일치하지 않습니다."),
    PIN_ALREADY_EXISTS(400, "지갑이 이미 존재합니다."),
    PIN_NOT_AUTHENTICATED(401, "기존의 핀번호와 일치하지 않습니다."),
    PIN_NOT_EXISTS(404, "지갑 정보를 찾을 수 없습니다."),
	FORBIDDEN_ACCESS_MEMBER(403, "접근 권한이 없는 사용자입니다."),
	// SYNC_ACCOUNT
	SYNC_ACCOUNT_NOT_FOUND(404, "요청 연동 계좌가 존재하지 않습니다"),
	// EXTERNAL_SERVER
	TWINKLE_BANK_SERVER_ERROR(500, "반짝 은행 API 요청에 실패했습니다"),
	CURRENCY_RATE_LOAD_SERVER_ERROR(500, "환율 조회 API 요청에 실패했습니다"),
	// TRIP_ACCOUNT
	CURRENCY_NOT_FOUND(404, "통화를 찾을 수 없습니다."),
	TRIP_ACCOUNT_NOT_FOUND(404, "해당 여행 계좌를 찾을 수 없습니다."),
	TRIP_ACCOUNT_BALANCE_BAD_REQUEST(400, "잔액보다 큰 출금은 불가능합니다."),
	// ATTRACTION
	REGION_NOT_FOUND(404, "지역을 찾을 수 없습니다"),
	ATTRACTION_NOT_FOUND(404, "여행지를 찾을 수 없습니다"),
	// PLAN
	PLAN_SAVE_BAD_REQUEST(400, "해당 기간에 여행 계획이 존재합니다."),
	PLAN_DATE_BAD_REQUEST(400, "종료 날짜가 시작 날짜보다 작을 수 없습니다."),
	PLAN_NOT_FOUND(404, "여행 계획을 찾을 수 없습니다."),
	// REVIEW
	REVIEW_NOT_FOUND(404, "여행지를 찾을 수 없습니다"),


	BAD_REQUEST(400, "잘못된 요청입니다."),

	CERT_CODE_NOT_MATCH(400, "입력된 입력코드가 일치하지 않습니다."),

	WRONG_PASSWORD(401, "사용자 정보를 찾을 수 없습니다."),

	USER_NOT_KAKAO_JOINED(421, "카카오 가입 회원이 아닙니다."), // 커스텀 에러 사용 예시

	FORBIDDEN(403, "접근할 수 없습니다."),

	COMMENT_NOT_FOUND(404, "독후감을 찾을 수 없습니다."),

	CERT_CODE_EXPIRED(408, "코드가 만료되었습니다."),

	ALREADY_KAKAO_JOINED(409, "이미 카카오가입을 한 사용자입니다."),
	;

	private final int status;
	private final String message;
}
