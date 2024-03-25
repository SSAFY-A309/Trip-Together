package com.ssafy.twinklebank.global.exception.handler;

import com.ssafy.twinklebank.global.exception.exceptions.category.*;
import com.ssafy.twinklebank.global.exception.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BadRequestException.class)
	public ErrorResponse badRequestHandler(BankRuntimeException exception) {
		log.error(exception.getMessageKey(), exception, exception.getParams());
		return new ErrorResponse(exception.getErrorCode());
	} // 400

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(UnAuthorizedException.class)
	public ErrorResponse unAuthorizedHandler(BankRuntimeException exception) {
		log.error(exception.getMessageKey(), exception, exception.getParams());
		return new ErrorResponse(exception.getErrorCode());
	} // 401

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(ForbiddenException.class)
	public ErrorResponse forbiddenHandler(BankRuntimeException exception) {
		log.error(exception.getMessageKey(), exception, exception.getParams());
		return new ErrorResponse(exception.getErrorCode());
	} // 403

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	public ErrorResponse notFoundHandler(BankRuntimeException exception) {
		log.error(exception.getMessageKey(), exception, exception.getParams());
		return new ErrorResponse(exception.getErrorCode());
	} // 404

	@ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
	@ExceptionHandler(TimeOutException.class)
	public ErrorResponse timeoutHandler(BankRuntimeException exception) {
		log.error(exception.getMessageKey(), exception, exception.getParams());
		return new ErrorResponse(exception.getErrorCode());
	} // 408

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(ConflictException.class)
	public ErrorResponse conflictHandler(BankRuntimeException exception) {
		log.error(exception.getMessageKey(), exception, exception.getParams());
		return new ErrorResponse(exception.getErrorCode());
	} // 409

	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	@ExceptionHandler(UnsupportedMediaTypeException.class)
	public ErrorResponse unsupportedMediaTypeHandler(BankRuntimeException exception) {
		log.error(exception.getMessageKey(), exception, exception.getParams());
		return new ErrorResponse(exception.getErrorCode());
	} // 415

	@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
	@ExceptionHandler(IAmATeapotException.class)
	public ErrorResponse iAmATeapotHandler(BankRuntimeException exception) {
		log.error(exception.getMessageKey(), exception, exception.getParams());
		return new ErrorResponse(exception.getErrorCode());
	} // 418

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(IllegalMonitorStateException.class)
	public ErrorResponse illegalMonitorStateHandler(IllegalMonitorStateException exception) {
		log.error(exception.getMessage(), exception);
		return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
	} // 500
}
