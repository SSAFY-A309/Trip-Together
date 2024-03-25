package com.ssafy.triptogether.global.exception.exceptions.category;

import com.ssafy.triptogether.global.exception.response.ErrorCode;

/**
 * 409
 * 리소스 등의 충돌
 */
public class ConflictException extends TripRuntimeException {
    protected static final String MESSAGE_KEY = "error.Conflict";

    public ConflictException(String detailMessageKey, ErrorCode errorCode, Object[] params) {
        super(MESSAGE_KEY + "." + detailMessageKey, errorCode, params);
    }
}
