package com.ihorak.truffle.exceptions;

public class ParserException extends IllegalArgumentException {

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParserException(String s) {
        super(s);
    }
}
