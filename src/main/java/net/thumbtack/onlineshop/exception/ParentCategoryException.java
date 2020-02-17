package net.thumbtack.onlineshop.exception;

public class ParentCategoryException extends RuntimeException {

    private GlobalExceptionErrorCode globalExceptionErrorCode;

    public ParentCategoryException(GlobalExceptionErrorCode globalExceptionErrorCode) {
        this.globalExceptionErrorCode = globalExceptionErrorCode;
    }

    public GlobalExceptionErrorCode getGlobalExceptionErrorCode() {
        return globalExceptionErrorCode;
    }
}
