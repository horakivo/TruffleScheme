package com.ihorak.truffle.node.callable.TCO.exceptions;

import com.oracle.truffle.api.nodes.ControlFlowException;

public class TailRecursiveException extends ControlFlowException {


    private TailRecursiveException() {}

    public static final TailRecursiveException INSTANCE = new TailRecursiveException();
}
