package net.thumbtack.onlineshop.utils.parsercl;

public class ParserException extends Exception {

    private ParserErrorCode parserErrorCode;

    public ParserException(ParserErrorCode parserErrorCode) {
        this.parserErrorCode = parserErrorCode;
    }

    public ParserErrorCode getParserErrorCode() {
        return parserErrorCode;
    }

}
