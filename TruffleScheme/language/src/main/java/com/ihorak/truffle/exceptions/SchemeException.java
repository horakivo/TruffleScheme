package com.ihorak.truffle.exceptions;

import com.oracle.truffle.api.exception.AbstractTruffleException;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;

public class SchemeException extends AbstractTruffleException {

    @TruffleBoundary
    public SchemeException(String message, Node location) {
        super(message, location);
    }
}
