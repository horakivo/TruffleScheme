package com.ihorak.truffle.convertor;

import com.ihorak.truffle.exceptions.SchemeException;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.exception.AbstractTruffleException;
import com.oracle.truffle.api.interop.ArityException;
import com.oracle.truffle.api.nodes.Node;

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

    public static ConverterException contractViolation(String operationName, String expected, Object given) {
        StringBuilder sb = new StringBuilder();

        sb.append(operationName).append(": ").append("contract violation\n");
        sb.append("expected: ").append(expected).append("\n");
        sb.append("given: ").append(given);


        return new ConverterException(sb.toString());
    }

    public static ConverterException definitionNotAllowed(String operationName) {
        return new ConverterException(operationName + ": " + "not allowed in an expression context");
    }

    public static ConverterException shouldNotReachHere() {
        return new ConverterException(null);
    }

    public static ConverterException shouldNotReachHere(String message) {
        return new ConverterException(message);
    }


}
