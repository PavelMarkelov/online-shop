package net.thumbtack.onlineshop.exception.dto;

import net.thumbtack.onlineshop.exception.GlobalExceptionErrorCode;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorDto {

	private final List<FieldErrorDto> errors = new ArrayList<>();

	public ValidationErrorDto() {
	}

	public void addFieldError(GlobalExceptionErrorCode errorCode, String field, String message) {
		FieldErrorDto error = new FieldErrorDto(errorCode, field, message);
		errors.add(error);
	}

	public List<FieldErrorDto> getErrors() {
		return errors;
	}
}
