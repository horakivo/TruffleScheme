package com.ihorak.truffle.node.polyglot;

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
    public static PolyglotException unsupportedMessageException(UnsupportedMessageException exception, Object receiver, String messageName, Node node) {
        StringBuilder sb = new StringBuilder();
        sb.append("Receiver: ").append(receiver).append(" doesn't support ").append(messageName).append(" message.\n");
        sb.append("Original exception message: ").append(exception.getMessage());

        return new PolyglotException(sb.toString(), node);
    }

    @TruffleBoundary
    public static PolyglotException unknownIdentifierException(UnknownIdentifierException exception, Object receiver, Node node) {
        StringBuilder sb = new StringBuilder();
        sb.append("Receiver: ").append(receiver).append(" doesn't have member with name ").append(exception.getUnknownIdentifier()).append("\n");
        sb.append("Original exception message: ").append(exception.getMessage());

        return new PolyglotException(sb.toString(), node);
    }

    @TruffleBoundary
    public static PolyglotException unsupportedTypeException(UnsupportedTypeException exception, Object receiver, Node node) {
        StringBuilder sb = new StringBuilder();
        sb.append("Receiver: ").append(receiver).append(": contract violation. Unexpected argument type\n");
        sb.append("Given: ").append(Arrays.toString(exception.getSuppliedValues())).append("\n");
        sb.append("Original exception message: ").append(exception.getMessage());

        return new PolyglotException(sb.toString(), node);
    }

    @TruffleBoundary
    public static PolyglotException arityException(ArityException exception, Object receiver, Object[] arguments, Node node) {
        StringBuilder sb = new StringBuilder();
        sb.append(receiver).append(": contract violation. Wrong number of arguments\n");
        sb.append("Expected: ").append(exception.getActualArity()).append("\n");
        sb.append("Given: ").append(arguments.length).append("\n");
        sb.append("Original exception message: ").append(exception.getMessage());
        return new PolyglotException(sb.toString(), node);
    }

    @TruffleBoundary
    public static PolyglotException invalidArrayIndexException(InvalidArrayIndexException exception, Object receiver, Object[] arguments, String message, Node node) {
        StringBuilder sb = new StringBuilder();
        sb.append(message).append(": invalid index\n");
        sb.append("index: ").append(exception.getInvalidIndex());
        sb.append("in: ").append(receiver).append("\n");
        sb.append("Original exception message: ").append(exception.getMessage());
        return new PolyglotException(sb.toString(), node);
    }


    @TruffleBoundary
    public static PolyglotException wrongMessageIdentifierType(String operationName, Object identifier, Node node) {
        StringBuilder sb = new StringBuilder();
        sb.append(operationName).append(": wrong identifier type.\n");
        sb.append("Expected: Symbol?\n");
        sb.append("Given: ").append(identifier);

        throw new PolyglotException(sb.toString(), node);
    }
}
