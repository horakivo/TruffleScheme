package com.ihorak.truffle.node.callable.TCO.throwers;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.TCO.exceptions.PolyglotTailCallException;
import com.oracle.truffle.api.dsl.Executed;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import java.util.List;

public abstract class PolyglotTailCallThrowerNode extends SchemeExpression {

    @Children
    private final SchemeExpression[] arguments;

    // this can be only either eval-source or p-proc
    @Child
    @Executed
    protected SchemeExpression polyCallable;

    private final Object operand;

    public PolyglotTailCallThrowerNode(List<SchemeExpression> arguments, SchemeExpression polyCallable, Object operand) {
        this.arguments = arguments.toArray(SchemeExpression[]::new);
        this.polyCallable = polyCallable;
        this.operand = operand;
    }


    @Specialization(guards = "interop.isExecutable(polyglotProc)", limit = "getInteropCacheLimit()")
    protected PolyglotTailCallException doThrow(VirtualFrame frame, Object polyglotProc,
                                                @CachedLibrary("polyglotProc") InteropLibrary interop) {
        throw new PolyglotTailCallException(polyglotProc, getArguments(frame));
    }

    @Fallback
    protected Object fallback(VirtualFrame frame, Object obj) {
        return null;
    }


    @ExplodeLoop
    private Object[] getArguments(VirtualFrame frame) {
        Object[] args = new Object[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            args[i] = arguments[i].executeGeneric(frame);
        }

        return args;
    }
}
