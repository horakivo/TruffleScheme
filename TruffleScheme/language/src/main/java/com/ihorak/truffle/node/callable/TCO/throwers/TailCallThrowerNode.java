package com.ihorak.truffle.node.callable.TCO.throwers;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.callable.DispatchNode;
import com.ihorak.truffle.node.callable.TCO.exceptions.TailCallException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.UserDefinedProcedure;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Executed;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import java.util.List;

public abstract class TailCallThrowerNode extends SchemeExpression {

    @Children
    private final SchemeExpression[] arguments;

    @Child
    @Executed
    protected SchemeExpression callable;

    //This can be either name or e.g. list which will evaluate to procedure
    private final Object operand;


    public TailCallThrowerNode(final List<SchemeExpression> arguments, final SchemeExpression callable, Object operand) {
        this.arguments = arguments.toArray(SchemeExpression[]::new);
        this.callable = callable;
        this.operand = operand;
    }

    @Specialization
    protected TailCallException doThrow(VirtualFrame frame, UserDefinedProcedure procedure) {
        throw new TailCallException(procedure, getProcedureArguments(procedure, arguments, frame));
    }


    @Specialization(guards = "interopLibrary.isExecutable(procedure)", limit = "getInteropCacheLimit()")
    protected Object doPolyglotThrow(VirtualFrame frame, Object procedure,
                                     @CachedLibrary("procedure") InteropLibrary interopLibrary,
                                     @Cached DispatchNode dispatchNode) {
        return dispatchNode.executeDispatch(procedure, getForeignProcedureArguments(arguments, frame));
    }


    @Fallback
    protected Object fallback(Object procedure) {
        throw SchemeException.notProcedure(procedure, this);
    }


    @Override
    public String toString() {
        return operand.toString();
    }
}
