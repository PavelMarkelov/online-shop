package net.thumbtack.onlineshop.utils.parsercl;

public enum ParserErrorCode {

        WRONG_ARGS("Invalid command line arguments"),
        WRONG_FILE_EXTENSION("Available file extension .yml");

        private String errorString;

        ParserErrorCode(String errorString) {
            this.errorString = errorString;
        }

        public String getErrorString() {
            return errorString;
        }
}
