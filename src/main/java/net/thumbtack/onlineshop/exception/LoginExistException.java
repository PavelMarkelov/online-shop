package net.thumbtack.onlineshop.exception;

public class LoginExistException extends RuntimeException {

    private GlobalExceptionErrorCode globalExceptionErrorCode;

    public LoginExistException(GlobalExceptionErrorCode globalExceptionErrorCode) {
        this.globalExceptionErrorCode = globalExceptionErrorCode;
    }

    public GlobalExceptionErrorCode getGlobalExceptionErrorCode() {
        return globalExceptionErrorCode;
    }
}
