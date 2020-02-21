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
        BAD_ORDER("Incorrect sort order. Possible value: \"product\" or \"category\""),
        ERROR_COUNT("The required quantity of the product is not in stock"),
        NO_MONEY("Not enough money in the account to buy the product in the specified quantity"),
        BUY_ERROR("Incorrectly stated the name or price of the product"),
        ERROR_BASKET("Not enough money in the account to buy the products in the specified quantity");


        private String errorString;

        GlobalExceptionErrorCode(String errorString) {
            this.errorString = errorString;
        }

        public String getErrorString() {
            return errorString;
        }
}
