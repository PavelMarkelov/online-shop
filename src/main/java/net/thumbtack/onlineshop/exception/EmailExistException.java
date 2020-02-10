package net.thumbtack.onlineshop.exception;

public class EmailExistException extends Exception {

    private ExceptionErrorCode exceptionErrorCode;

    public EmailExistException(ExceptionErrorCode exceptionErrorCode) {
        this.exceptionErrorCode = exceptionErrorCode;
    }

    public ExceptionErrorCode getExceptionErrorCode() {
        return exceptionErrorCode;
    }
}
