package com.ihorak.truffle.node.callable.TCO.exceptions;

import com.oracle.truffle.api.nodes.ControlFlowException;

public class SelfRecursiveTailCallException extends ControlFlowException {

    public static final SelfRecursiveTailCallException INSTANCE = new SelfRecursiveTailCallException();
}
