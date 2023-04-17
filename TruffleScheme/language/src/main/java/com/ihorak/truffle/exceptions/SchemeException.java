package com.ihorak.truffle.exceptions;

import com.ihorak.truffle.runtime.SchemeSymbol;
import com.oracle.truffle.api.exception.AbstractTruffleException;
import com.oracle.truffle.api.interop.InteropException;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;

public class SchemeException extends AbstractTruffleException {

    @TruffleBoundary
    public SchemeException(String message, Node location) {
        super(message, location);
    }

    @TruffleBoundary
    public static SchemeException contractViolation(Node node, String operationName, String expected, Object given) {
        StringBuilder sb = new StringBuilder();

        sb.append(operationName).append(": ").append("contract violation\n");
        sb.append("expected: ").append(expected).append("\n");
        sb.append("given: ").append(given);


        return new SchemeException(sb.toString(), node);
    }

    @TruffleBoundary
    public static SchemeException arityException(Node node, String name, int expected, int given) {
        String msg = name + ": arity mismatch; Expected number of arguments does not match the given number\n" +
                "expected: " + expected + "\n" +
                "given: " + given;

        return new SchemeException(msg, node);
    }

    @TruffleBoundary
    public static SchemeException arityExceptionAtLeast(Node node, String name, int expected, int given) {
        String msg = name + ": arity mismatch; Expected number of arguments does not match the given number\n" +
                "expected: at least " + expected + "\n" +
                "given: " + given;

        return new SchemeException(msg, node);
    }


    @TruffleBoundary
    public static SchemeException interopException(InteropException exception) {
        return new SchemeException(exception.getMessage(), null);
    }

    @TruffleBoundary
    public static SchemeException exception(Node node, String message) {
        return new SchemeException(message, node);
    }

    @TruffleBoundary
    public static SchemeException contractViolation(Node node, String operationName, String expected, Object left, Object right) {
        StringBuilder sb = new StringBuilder();

        sb.append(operationName).append(": ").append("contract violation\n");
        sb.append("expected: ").append(expected).append("\n");
        sb.append("given left: ").append(left).append("\n");
        sb.append("given right: ").append(right);


        return new SchemeException(sb.toString(), node);
    }

    @TruffleBoundary
    public static SchemeException undefinedIdentifier(Node node, SchemeSymbol name) {
        StringBuilder sb = new StringBuilder();

        sb.append(name.getValue()).append(": ").append("undefined\n");
        sb.append("cannot reference an identifier before its definition");

        return new SchemeException(sb.toString(), node);
    }


    public static SchemeException shouldNotReachHere(String message, Node location) {
        throw new SchemeException(message, location);
    }

    public static SchemeException shouldNotReachHere() {
        throw new SchemeException(null, null);
    }

    @TruffleBoundary
    public static SchemeException notProcedure(Object notProcedureObject, Node node) {
        StringBuilder sb = new StringBuilder();
        sb.append("Application: not a procedure\n");
        sb.append("Expected: procedure that can be applied to arguments\n");
        sb.append("Given: ").append(notProcedureObject);

        throw new SchemeException(sb.toString(), node);
    }
}
