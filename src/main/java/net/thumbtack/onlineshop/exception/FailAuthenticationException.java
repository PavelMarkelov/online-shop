package net.thumbtack.onlineshop.exception;

public class FailAuthenticationException  extends RuntimeException {

    private GlobalExceptionErrorCode globalExceptionErrorCode;

    public FailAuthenticationException(GlobalExceptionErrorCode globalExceptionErrorCode) {
        this.globalExceptionErrorCode = globalExceptionErrorCode;
    }

    public GlobalExceptionErrorCode getGlobalExceptionErrorCode() {
        return globalExceptionErrorCode;
    }
}
