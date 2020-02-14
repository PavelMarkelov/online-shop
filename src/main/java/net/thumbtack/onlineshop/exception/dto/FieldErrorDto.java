package net.thumbtack.onlineshop.exception.dto;


import net.thumbtack.onlineshop.exception.GlobalExceptionErrorCode;

public class FieldErrorDto {

	private final String errorCode;
	private final String field;
	private final String message;

	public FieldErrorDto(GlobalExceptionErrorCode errorCode, String field, String message) {
		this.errorCode = String.format("%s", errorCode);
		this.field = field;
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getField() {
		return field;
	}

	public String getMessage() {
		return message;
	}
}
