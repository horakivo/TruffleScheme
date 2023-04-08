package com.ihorak.truffle.convertor;

import com.oracle.truffle.api.exception.AbstractTruffleException;
import com.oracle.truffle.api.interop.ArityException;

public class ConverterException extends AbstractTruffleException {


    public ConverterException(String message) {
        super(message);
    }


    public static ConverterException arityException(String name, int expected, int given) {
        String sb = name + ": arity mismatch; Expected number of arguments does not match the given number\n" +
                "expected: " + expected + "\n" +
                "given: " + given;

//        StringBuilder sb = new StringBuilder();
//        sb.append(name).append(": arity mismatch; Expected number of arguments does not match the given number\n");
//        sb.append("expected: ").append(expected).append("\n");
//        sb.append("given: ").append(given);

        return new ConverterException(sb);
    }


}
