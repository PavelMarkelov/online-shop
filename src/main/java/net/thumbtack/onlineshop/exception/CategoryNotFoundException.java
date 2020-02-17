package net.thumbtack.onlineshop.exception;

public class CategoryNotFoundException extends RuntimeException {

    private GlobalExceptionErrorCode globalExceptionErrorCode;

    public CategoryNotFoundException(GlobalExceptionErrorCode globalExceptionErrorCode) {
        this.globalExceptionErrorCode = globalExceptionErrorCode;
    }

    public GlobalExceptionErrorCode getGlobalExceptionErrorCode() {
        return globalExceptionErrorCode;
    }

}
