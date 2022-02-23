package com.ihorak.truffle.parser;

public class ParserException extends IllegalArgumentException {

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParserException(String s) {
        super(s);
    }
}
