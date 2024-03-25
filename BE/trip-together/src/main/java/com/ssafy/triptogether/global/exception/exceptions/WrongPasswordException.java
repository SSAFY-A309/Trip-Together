package com.ssafy.triptogether.global.exception.exceptions;

import com.ssafy.triptogether.global.exception.exceptions.category.UnAuthorizedException;
import com.ssafy.triptogether.global.exception.response.ErrorCode;

public class WrongPasswordException extends UnAuthorizedException {
    private static final ErrorCode errorCode = ErrorCode.WRONG_PASSWORD;

    public WrongPasswordException(String detailMessageKey, Object... params) {
        super(errorCode.getMessage() + " : " + detailMessageKey, errorCode, params);
    }
}
