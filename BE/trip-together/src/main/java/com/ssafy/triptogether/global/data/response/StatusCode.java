package com.ssafy.triptogether.global.data.response;

import lombok.Getter;

@Getter
public enum StatusCode {
    // AUTH
    SUCCESS_PIN_VERIFY(200, "PIN 인증 성공"),
    // PLAN
    SUCCESS_PLANS_SAVE(201, "여행 계획 저장 성공"),
    SUCCESS_PLAN_DELETE(204, "여행 계획 삭제 성공"),
    SUCCESS_PLAN_DETAIL_FIND(200, "여행 계획 상세 조회 성공"),
    //ATTRACTION
    SUCCESS_ATTRACTION_DETAIL_FIND(200, "여행지별 명소 상세 조회 성공"),
    // TRIP_ACCOUNT
    SUCCESS_CURRENCY_LOAD(200, "환전 가능 통화 목록 조회 성공"),
    SUCCESS_RATE_LOAD(200, "요청 통화 환율 조회 성공"),
    SUCCESS_TRIP_ACCOUNTS_LOAD(200, "지갑 내 목록 조회 성공"),
    SUCCESS_ACCOUNT_HISTORIES_LOAD(200, "거래 내역 조회 성공"),
    SUCCESS_TRIP_ACCOUNT_EXCHANGE(200, "환전 성공"),
    //SYNC_ACCOUNT
    SUCCESS_SYNC_ACCOUNTS_LOAD(200, "연동 계좌 목록 조회 성공"),
    SUCCESS_MAIN_SYNC_ACCOUNT_UPDATE(200, "주계좌 변경 성공"),
    SUCCESS_BANK_ACCOUNTS_LOAD(200, "회원 은행 계좌 목록 조회 성공"),
    SUCCESS_SYNC_ACCOUNTS_SAVE(201, "연동 계좌 등록 성공"),
    SUCCESS_SYNC_ACCOUNT_DELETE(204, "연동 계좌 해지 성공"),
    // MEMBER
    SUCCESS_PROFILE_UPDATE(200, "프로필 수정 성공"),
    SUCCESS_PROFILE_FIND(200, "프로필 조회 성공"),
    SUCCESS_PIN_SAVE(200, "pin 번호 등록 성공"),
    SUCCESS_PIN_UPDATE(200, "pin 번호 수정 성공"),
    SUCCESS_LOGOUT(200, "로그아웃 성공");

    private final int status;
    private final String message;

    StatusCode(final int status, final String message) {
        this.status = status;
        this.message = message;
    }
}
