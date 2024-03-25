package com.ssafy.triptogether.global.exception.exceptions.category;

import com.ssafy.triptogether.global.exception.response.ErrorCode;

public class IAmATeapotException extends TripRuntimeException {
    protected static String MESSAGE_KEY = "error.I'mATeapot";

    public IAmATeapotException(String detailMessageKey, ErrorCode errorCode, Object... params) {
        super(MESSAGE_KEY + "." + detailMessageKey, errorCode, params);
    }
}
