package net.thumbtack.onlineshop.exception;

public class CategoryExistException extends RuntimeException {

    private GlobalExceptionErrorCode globalExceptionErrorCode;

    public CategoryExistException(GlobalExceptionErrorCode globalExceptionErrorCode) {
        this.globalExceptionErrorCode = globalExceptionErrorCode;
    }

    public GlobalExceptionErrorCode getGlobalExceptionErrorCode() {
        return globalExceptionErrorCode;
    }

}
