package com.ihorak.truffle.exceptions;

import com.ihorak.truffle.type.SchemeSymbol;
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
    public static SchemeException interopException(InteropException exception) {
        return new SchemeException(exception.getMessage(), null);
    }

    @TruffleBoundary
    public static SchemeException interopArrayElementIsNotRemovable() {
        var msg = "Unable to remove element since the foreign array is immutable or the element doesn't exist";
        return new SchemeException(msg, null);
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

    @TruffleBoundary
    public static SchemeException undefinedPolyglotIdentifier(Node node, SchemeSymbol name, SchemeSymbol languageId) {
        StringBuilder sb = new StringBuilder();

        sb.append(name.getValue()).append(": ").append("undefined\n");
        sb.append("cannot find ").append(name.getValue()).append(" in ").append(languageId.getValue()).append(" global scope.\n");
        sb.append("Was it defined using eval-source?");

        return new SchemeException(sb.toString(), node);
    }

    public static SchemeException shouldNotReachHere(String message, Node location) {
        throw new SchemeException(message, location);
    }
}
