package com.ihorak.truffle.exceptions;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.exception.AbstractTruffleException;
import com.oracle.truffle.api.nodes.Node;

public class SchemeException extends AbstractTruffleException {

    public SchemeException(String message) {
        this(null, message);
    }

    public SchemeException(Node location, String message) {
        super(message, location);
    }
}
