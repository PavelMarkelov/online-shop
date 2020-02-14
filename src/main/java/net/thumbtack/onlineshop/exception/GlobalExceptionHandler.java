package net.thumbtack.onlineshop.exception;

import net.thumbtack.onlineshop.exception.dto.ValidationErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;


@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity handleNotAuthenticatedException(BadCredentialsException ex) {
		ValidationErrorDto veDto = new ValidationErrorDto();
		veDto.addFieldError(GlobalExceptionErrorCode.NOT_LOGIN, "",
				ex.getMessage());
		return new ResponseEntity<>(veDto, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity handleNotAuthenticatedException(UsernameNotFoundException ex) {
		ValidationErrorDto veDto = new ValidationErrorDto();
		veDto.addFieldError(GlobalExceptionErrorCode.NOT_LOGIN, "",
				ex.getMessage());
		return new ResponseEntity<>(veDto, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(LoginExistException.class)
	public ResponseEntity<ValidationErrorDto> performLoginExistsException(LoginExistException ex) {
		ValidationErrorDto veDto = new ValidationErrorDto();
		veDto.addFieldError(ex.getGlobalExceptionErrorCode(), "login",
				ex.getGlobalExceptionErrorCode().getErrorString());
		return new ResponseEntity<>(veDto, HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(FailAuthenticationException.class)
	public ResponseEntity<ValidationErrorDto> performFailAuthException(FailAuthenticationException ex) {
		ValidationErrorDto veDto = new ValidationErrorDto();
		veDto.addFieldError(ex.getGlobalExceptionErrorCode(), "auth",
				ex.getGlobalExceptionErrorCode().getErrorString());
		return new ResponseEntity<>(veDto, HttpStatus.NOT_ACCEPTABLE);
	}


	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationErrorDto> processValidationError(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();
		ValidationErrorDto veDto = new ValidationErrorDto();
		for (FieldError fieldError : fieldErrors) {
			String field = fieldError.getField();
			String errorMessage = fieldError.getDefaultMessage();
			veDto.addFieldError(GlobalExceptionErrorCode.FORM_ERROR, field, errorMessage);
		}
		return new ResponseEntity<>(veDto, HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ValidationErrorDto> performBadRequestException(HttpMessageNotReadableException ex) {
		ValidationErrorDto veDto = new ValidationErrorDto();
		veDto.addFieldError(GlobalExceptionErrorCode.IS_MISSING, "body",
				ex.getMessage());
		return new ResponseEntity<>(veDto, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ValidationErrorDto> performNotSupportedException(HttpRequestMethodNotSupportedException ex) {
		ValidationErrorDto veDto = new ValidationErrorDto();
		veDto.addFieldError(GlobalExceptionErrorCode.NOT_SUPPORTED, "",
				ex.getMessage());
		return new ResponseEntity<>(veDto, HttpStatus.METHOD_NOT_ALLOWED);
	}
}
