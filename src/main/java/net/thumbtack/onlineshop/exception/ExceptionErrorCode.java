package net.thumbtack.onlineshop.exception;

public enum ExceptionErrorCode {

        LOGIN_EXIST("Login already exists");

        private String errorString;

        ExceptionErrorCode(String errorString) {
            this.errorString = errorString;
        }

        public String getErrorString() {
            return errorString;
        }
}
