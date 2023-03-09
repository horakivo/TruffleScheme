package com.ihorak.truffle.node.callable.TCO.exceptions;

import com.oracle.truffle.api.nodes.ControlFlowException;

public class SelfRecursiveTailCallException extends ControlFlowException {


    private SelfRecursiveTailCallException() {}

    public static final SelfRecursiveTailCallException INSTANCE = new SelfRecursiveTailCallException();
}
