package net.thumbtack.onlineshop.utils.propfilecheck;

public enum CheckerErrorCode {
    NOT_EXIST("The specified file does not exist"),
    MISSING_PROP("Required property not found");

    private String errorString;

    CheckerErrorCode(String errorString) {
        this.errorString = errorString;
    }

    public String getErrorString() {
        return errorString;
    }

}
