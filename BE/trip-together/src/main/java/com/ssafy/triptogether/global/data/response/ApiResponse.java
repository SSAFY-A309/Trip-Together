package com.ssafy.triptogether.global.data.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;

    /**
     * DefaultResponse 를 ResponseEntity 형식으로 반환하기 위한 메소드입니다.
     *
     * @param statusCode 응답 코드
     * @return StatusCode 의 정보를 담아 ResponseEntity 를 반환
     */
    public static <T> ResponseEntity<ApiResponse<T>> toResponseEntity(HttpStatus httpStatus,
                                                                      StatusCode statusCode, T data) {
        return ResponseEntity
                .status(httpStatus)
                .body(ApiResponse.<T>builder()
                        .status(statusCode.getStatus())
                        .message(statusCode.getMessage())
                        .data(data)
                        .build()
                );
    }

    public static <T> ResponseEntity<ApiResponse<T>> emptyResponse(HttpStatus httpStatus,
                                                                   StatusCode statusCode) {
        return ResponseEntity
                .status(httpStatus)
                .body(ApiResponse.<T>builder()
                        .status(statusCode.getStatus())
                        .message(statusCode.getMessage())
                        .build()
                );
    }
}