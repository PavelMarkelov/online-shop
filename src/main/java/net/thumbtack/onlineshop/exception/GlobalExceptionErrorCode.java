package net.thumbtack.onlineshop.exception;

public enum GlobalExceptionErrorCode {

        LOGIN_EXIST("Login already exists"),
        NOT_AUTH("You must first log in"),
        FORM_ERROR(""),
        FAIL_AUTH("Set authentication after save failed"),
        IS_MISSING("body"),
        NOT_SUPPORTED(""),
        NOT_LOGIN("You need to log in");

        private String errorString;

        GlobalExceptionErrorCode(String errorString) {
            this.errorString = errorString;
        }

        public String getErrorString() {
            return errorString;
        }
}
