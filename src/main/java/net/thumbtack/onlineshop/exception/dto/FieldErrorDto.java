package net.thumbtack.onlineshop.exception.dto;


public class FieldErrorDto {

	private final String errorCode;
	private final String field;
	private final String message;

	public FieldErrorDto(String errorCode, String field, String message) {
		this.errorCode = errorCode;
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
