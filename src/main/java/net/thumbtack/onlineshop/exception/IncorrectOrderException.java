package net.thumbtack.onlineshop.exception;

public class IncorrectOrderException extends RuntimeException {

    private GlobalExceptionErrorCode globalExceptionErrorCode;

    public IncorrectOrderException(GlobalExceptionErrorCode globalExceptionErrorCode) {
        this.globalExceptionErrorCode = globalExceptionErrorCode;
    }

    public GlobalExceptionErrorCode getGlobalExceptionErrorCode() {
        return globalExceptionErrorCode;
    }
}
