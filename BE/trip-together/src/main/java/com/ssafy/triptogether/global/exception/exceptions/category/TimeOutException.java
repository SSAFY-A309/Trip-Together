package com.ssafy.triptogether.global.exception.exceptions.category;

import com.ssafy.triptogether.global.exception.response.ErrorCode;

/**
 * 408 에러
 * 타임아웃
 */
public class TimeOutException extends TripRuntimeException {
    protected static final String MESSAGE_KEY = "error.TimeOut";

    public TimeOutException(String detailMessageKey, ErrorCode errorCode, Object... params) {
        super(MESSAGE_KEY + "." + detailMessageKey, errorCode, params);
    }
}
