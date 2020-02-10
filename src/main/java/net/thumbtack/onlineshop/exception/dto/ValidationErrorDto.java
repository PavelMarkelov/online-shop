package net.thumbtack.onlineshop.exception.dto;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorDto {

	private final List<FieldErrorDto> fieldErrors = new ArrayList<>();

	public ValidationErrorDto() {
	}

	public void addFieldError(String errorCode, String field, String message) {
		FieldErrorDto error = new FieldErrorDto(errorCode, field, message);
		fieldErrors.add(error);
	}

	public List<FieldErrorDto> getFieldErrors() {
		return fieldErrors;
	}
}
