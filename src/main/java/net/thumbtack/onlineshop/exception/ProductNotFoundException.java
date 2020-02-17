package net.thumbtack.onlineshop.exception;

public class ProductNotFoundException extends RuntimeException {

    private GlobalExceptionErrorCode globalExceptionErrorCode;

    public ProductNotFoundException(GlobalExceptionErrorCode globalExceptionErrorCode) {
        this.globalExceptionErrorCode = globalExceptionErrorCode;
    }

    public GlobalExceptionErrorCode getGlobalExceptionErrorCode() {
        return globalExceptionErrorCode;
    }

}
