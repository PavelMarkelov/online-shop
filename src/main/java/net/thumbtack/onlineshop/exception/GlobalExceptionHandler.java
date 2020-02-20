package net.thumbtack.onlineshop.exception;

import net.thumbtack.onlineshop.exception.dto.FieldErrorDto;
import net.thumbtack.onlineshop.exception.dto.ValidationErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
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
	public ResponseEntity handlerNotAuthenticatedException(BadCredentialsException ex) {
		FieldErrorDto field = new FieldErrorDto(GlobalExceptionErrorCode.NOT_LOGIN, "",
				ex.getMessage());
		return new ResponseEntity<>(field, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity handlerNotAuthenticatedException(UsernameNotFoundException ex) {
		FieldErrorDto field = new FieldErrorDto(GlobalExceptionErrorCode.NOT_LOGIN, "",
				ex.getMessage());
		return new ResponseEntity<>(field, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity handlerForbiddenException() {
		FieldErrorDto field = new FieldErrorDto(GlobalExceptionErrorCode.FORBIDDEN, "",
				GlobalExceptionErrorCode.FORBIDDEN.getErrorString());
		return new ResponseEntity<>(field, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(FailPasswordException.class)
	public ResponseEntity handlerBadPasswordException(FailPasswordException ex) {
		FieldErrorDto field = new FieldErrorDto(ex.getGlobalExceptionErrorCode(), "oldPassword",
				ex.getGlobalExceptionErrorCode().getErrorString());
		return new ResponseEntity<>(field, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(CategoryExistException.class)
	public ResponseEntity handlerCategoryExistException(CategoryExistException ex) {
		FieldErrorDto field = new FieldErrorDto(ex.getGlobalExceptionErrorCode(), "name",
				ex.getGlobalExceptionErrorCode().getErrorString());
		return new ResponseEntity<>(field, HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(CategoryNotFoundException.class)
	public ResponseEntity handlerCategoryNotFoundException(CategoryNotFoundException ex) {
		FieldErrorDto field = new FieldErrorDto(ex.getGlobalExceptionErrorCode(), "id",
				ex.getGlobalExceptionErrorCode().getErrorString());
		return new ResponseEntity<>(field, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ParentCategoryException.class)
	public ResponseEntity handlerParentCategoryException(ParentCategoryException ex) {
		FieldErrorDto field = new FieldErrorDto(ex.getGlobalExceptionErrorCode(), "parentId",
				ex.getGlobalExceptionErrorCode().getErrorString());
		return new ResponseEntity<>(field, HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(LoginExistException.class)
	public ResponseEntity handlerLoginExistsException(LoginExistException ex) {
		FieldErrorDto field = new FieldErrorDto(ex.getGlobalExceptionErrorCode(), "login",
				ex.getGlobalExceptionErrorCode().getErrorString());
		return new ResponseEntity<>(field, HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity handlerProductNotFoundException(ProductNotFoundException ex) {
		FieldErrorDto field = new FieldErrorDto(ex.getGlobalExceptionErrorCode(), "",
				ex.getGlobalExceptionErrorCode().getErrorString());
		return new ResponseEntity<>(field, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(IncorrectOrderException.class)
	public ResponseEntity handlerProductNotFoundException(IncorrectOrderException ex) {
		FieldErrorDto field = new FieldErrorDto(ex.getGlobalExceptionErrorCode(), "order",
				ex.getGlobalExceptionErrorCode().getErrorString());
		return new ResponseEntity<>(field, HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(NoMoneyException.class)
	public ResponseEntity handlerNoMoneyException(NoMoneyException ex) {
		FieldErrorDto field = new FieldErrorDto(ex.getGlobalExceptionErrorCode(), null,
				ex.getGlobalExceptionErrorCode().getErrorString());
		return new ResponseEntity<>(field, HttpStatus.NOT_ACCEPTABLE);
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
	public ResponseEntity handlerBadRequestException(HttpMessageNotReadableException ex) {
		FieldErrorDto field = new FieldErrorDto(GlobalExceptionErrorCode.IS_MISSING, "body",
				ex.getMessage());
		return new ResponseEntity<>(field, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity handlerNotSupportedException(HttpRequestMethodNotSupportedException ex) {
		FieldErrorDto field = new FieldErrorDto(GlobalExceptionErrorCode.NOT_SUPPORTED, "",
				ex.getMessage());
		return new ResponseEntity<>(field, HttpStatus.METHOD_NOT_ALLOWED);
	}
}
