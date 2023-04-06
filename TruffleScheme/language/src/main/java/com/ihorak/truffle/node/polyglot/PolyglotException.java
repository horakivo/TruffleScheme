package com.ihorak.truffle.node.polyglot;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.exception.AbstractTruffleException;
import com.oracle.truffle.api.interop.ArityException;
import com.oracle.truffle.api.interop.InteropException;
import com.oracle.truffle.api.interop.UnknownIdentifierException;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.interop.UnsupportedTypeException;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;

public class PolyglotException extends AbstractTruffleException {

    public PolyglotException(String message, Node location) {
        super(message, location);
    }

    @TruffleBoundary
    public static PolyglotException readMemberException(InteropException e, Object receiver, SchemeSymbol memberName, Node node) {
        if (e instanceof UnsupportedMessageException exception) {
            var message = unsupportedMessageText(exception, receiver, "ReadMember");
            return new PolyglotException(message, node);
        } else if (e instanceof UnknownIdentifierException exception) {
            StringBuilder sb = new StringBuilder();
            sb.append("Object: ").append(receiver).append(" doesn't have member with name ").append(memberName.getValue()).append("\n");
            sb.append("Original exception message: ").append(exception.getMessage());
            return new PolyglotException(sb.toString(), node);
        }

        throw SchemeException.shouldNotReachHere("Internal error: ReadMember throw unexpected exception", node);
    }

    @TruffleBoundary
    public static PolyglotException executeException(InteropException e, Object procedure, int numberOfArgs, Node node) {
        if (e instanceof ArityException exception) {
            StringBuilder sb = new StringBuilder();
            sb.append(procedure).append(": contract violation. Wrong number of arguments\n");
            sb.append("Expected: ").append(exception.getActualArity()).append("\n");
            sb.append("Given: ").append(numberOfArgs);
            return new PolyglotException(sb.toString(), node);
        } else if (e instanceof UnsupportedMessageException exception) {
            var message = unsupportedMessageText(exception, procedure, "Execute");
            return new PolyglotException(message, node);
        } else if (e instanceof UnsupportedTypeException exception) {
            StringBuilder sb = new StringBuilder();
            sb.append(procedure).append(": contract violation. Unexpected argument type\n");
            return new PolyglotException(sb.toString(), node);
        }

        throw SchemeException.shouldNotReachHere("Internal error: ReadMember throw unexpected exception", node);
    }

    @TruffleBoundary
    public static PolyglotException exception(String message, Node node) {
        throw new PolyglotException(message, node);
    }

    @TruffleBoundary
    public static PolyglotException wrongMessageIdentifierType(String operationName, Object identifier, Node node) {
        StringBuilder sb = new StringBuilder();
        sb.append(operationName).append(": wrong identifier type.\n");
        sb.append("Expected: Symbol?\n");
        sb.append("Given: ").append(identifier);

        throw new PolyglotException(sb.toString(), node);
    }

    private static String unsupportedMessageText(UnsupportedMessageException exception, Object receiver, String messageName) {
        StringBuilder sb = new StringBuilder();
        sb.append("Receiver: ").append(receiver).append(" doesn't support ").append(messageName).append(" message.\n");
        sb.append("Original exception message: ").append(exception.getMessage());

        return sb.toString();
    }


}
