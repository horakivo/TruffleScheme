package com.ihorak.truffle.exceptions;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.exception.AbstractTruffleException;
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
}
