package com.ihorak.truffle.node.builtin.polyglot;

import com.oracle.truffle.api.exception.AbstractTruffleException;
import com.oracle.truffle.api.interop.ArityException;
import com.oracle.truffle.api.interop.InvalidArrayIndexException;
import com.oracle.truffle.api.interop.UnknownIdentifierException;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.interop.UnsupportedTypeException;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;

import java.util.Arrays;

public class PolyglotException extends AbstractTruffleException {

    public PolyglotException(String message, Node location) {
        super(message, location);
    }


    @TruffleBoundary
    public static PolyglotException unsupportedMessageException(UnsupportedMessageException exception, Object receiver, Node node) {
        StringBuilder sb = new StringBuilder();
        sb.append("Message is not supported\n");
        sb.append("receiver: ").append(receiver).append("\n");
        sb.append("Original exception message: ").append(exception.getMessage());

        return new PolyglotException(sb.toString(), node);
    }

    @TruffleBoundary
    public static PolyglotException unknownIdentifierException(UnknownIdentifierException exception, Object receiver, Node node) {
        StringBuilder sb = new StringBuilder();
        sb.append("Unknown identifier\n");
        sb.append("receiver: ").append(receiver).append("\n");
        sb.append("identifier: ").append(exception.getUnknownIdentifier()).append("\n");
        sb.append("Original exception message: ").append(exception.getMessage());

        return new PolyglotException(sb.toString(), node);
    }

    @TruffleBoundary
    public static PolyglotException unsupportedTypeException(UnsupportedTypeException exception, Object receiver, Node node) {
        StringBuilder sb = new StringBuilder();
        sb.append("Unexpected argument type\n");
        sb.append("receiver: ").append(receiver).append("\n");
        sb.append("given: ").append(Arrays.toString(exception.getSuppliedValues())).append("\n");
        sb.append("Original exception message: ").append(exception.getMessage());

        return new PolyglotException(sb.toString(), node);
    }

    @TruffleBoundary
    public static PolyglotException arityException(ArityException exception, Object receiver, Node node) {
        StringBuilder sb = new StringBuilder();
        sb.append("Wrong number of arguments\n");
        sb.append("receiver: ").append(receiver).append("\n");
        sb.append("expected (min): ").append(exception.getExpectedMinArity()).append("\n");
        sb.append("expected (max): ").append(exception.getExpectedMaxArity()).append("\n");
        sb.append("given: ").append(exception.getActualArity()).append("\n");
        sb.append("Original exception message: ").append(exception.getMessage());
        return new PolyglotException(sb.toString(), node);
    }

    @TruffleBoundary
    public static PolyglotException invalidArrayIndexException(InvalidArrayIndexException exception, Object receiver, Node node) {
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid array index");
        sb.append("receiver: ").append(receiver).append("\n");
        sb.append("index: ").append(exception.getInvalidIndex());
        sb.append("Original exception message: ").append(exception.getMessage());
        return new PolyglotException(sb.toString(), node);
    }

}
