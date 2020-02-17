package net.thumbtack.onlineshop.exception;

public class FailPasswordException extends RuntimeException {

    private GlobalExceptionErrorCode globalExceptionErrorCode;

    public FailPasswordException(GlobalExceptionErrorCode globalExceptionErrorCode) {
        this.globalExceptionErrorCode = globalExceptionErrorCode;
    }

    public GlobalExceptionErrorCode getGlobalExceptionErrorCode() {
        return globalExceptionErrorCode;
    }
}
