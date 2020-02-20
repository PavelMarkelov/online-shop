package net.thumbtack.onlineshop.exception;

public class NoMoneyException extends RuntimeException {

    private GlobalExceptionErrorCode globalExceptionErrorCode;

    public NoMoneyException(GlobalExceptionErrorCode globalExceptionErrorCode) {
        this.globalExceptionErrorCode = globalExceptionErrorCode;
    }

    public GlobalExceptionErrorCode getGlobalExceptionErrorCode() {
        return globalExceptionErrorCode;
    }
}
