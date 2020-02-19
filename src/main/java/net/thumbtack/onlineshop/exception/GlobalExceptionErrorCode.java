package net.thumbtack.onlineshop.exception;

public enum GlobalExceptionErrorCode {

        LOGIN_EXIST("Login already exists"),
        NOT_AUTH("You must first log in"),
        FORM_ERROR(""),
        FAIL_AUTH("Set authentication after save failed"),
        IS_MISSING("body"),
        NOT_SUPPORTED(""),
        NOT_LOGIN("You need to log in"),
        FORBIDDEN("Access denied"),
        BAD_PASSWORD("Old password is not valid"),
        CATEGORY_EXIST("Category with the specified name already exists"),
        CATEGORY_NOT_FOUND("Category with the specified id not exists"),
        PARENT_CATEGORY("Forbidden to move a category to a subcategory"),
        CHILD_CATEGORY("Forbidden add child category to a subcategory"),
        ERROR_CHILD("Forbidden to move a subcategory to a category"),
        PARENT_CATEGORY_NOT_EXIST("Parent category does not exist"),
        PRODUCT_NOT_FOUND("Product with the specified id not exists"),
        BAD_ORDER("Incorrect sort order. Possible value: \"product\" or \"category\"");


        private String errorString;

        GlobalExceptionErrorCode(String errorString) {
            this.errorString = errorString;
        }

        public String getErrorString() {
            return errorString;
        }
}
