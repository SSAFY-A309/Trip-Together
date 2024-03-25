package com.ssafy.triptogether.global.exception.exceptions.category;

import com.ssafy.triptogether.global.exception.response.ErrorCode;

/**
 * 500 에러
 * 외부 서버와의 통신 오류
 */
public class ExternalServerException extends TripRuntimeException {
    protected static final String MESSAGE_KEY = "error.ExternalServer";

    public ExternalServerException(String detailMessageKey, ErrorCode errorCode, Object... params) {
        super(MESSAGE_KEY + "." + detailMessageKey, errorCode, params);
    }
}
