package br.com.wallet.control.web.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class APIErrorHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(value = { UserAlreadyExistsException.class, EntityNotFoundException.class })
	protected ResponseEntity<Object> handleExistentUser(RuntimeException ex, WebRequest request) {
		ApiErrorDTO errorDTO = new ApiErrorDTO(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
		return handleExceptionInternal(ex, errorDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

}
