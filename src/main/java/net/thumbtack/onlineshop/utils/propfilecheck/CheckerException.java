package net.thumbtack.onlineshop.utils.propfilecheck;

public class CheckerException extends Exception {
    private CheckerErrorCode checkerErrorCode;

    public CheckerException(CheckerErrorCode checkerErrorCode) {
        this.checkerErrorCode = checkerErrorCode;
    }

    public CheckerErrorCode getCheckerErrorCode() {
        return checkerErrorCode;
    }
}
