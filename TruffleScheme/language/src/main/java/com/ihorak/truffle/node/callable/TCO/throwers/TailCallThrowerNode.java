package com.ihorak.truffle.node.callable.TCO.throwers;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.callable.DispatchUserProcedureNode;
import com.ihorak.truffle.node.callable.DispatchPrimitiveProcedureNode;
import com.ihorak.truffle.node.callable.TCO.exceptions.TailCallException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.runtime.PrimitiveProcedure;
import com.ihorak.truffle.runtime.UserDefinedProcedure;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Executed;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;

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

    @Specialization
    protected Object doPrimitiveProcedure(
            VirtualFrame frame,
            PrimitiveProcedure procedure,
            @Cached DispatchPrimitiveProcedureNode dispatchNode) {

        return dispatchNode.execute(procedure, getArgumentsWithoutLexicalScope(arguments, frame));
    }


    @Specialization(guards = "interopLibrary.isExecutable(procedure)", limit = "getInteropCacheLimit()")
    protected Object doPolyglot(VirtualFrame frame, Object procedure,
                                @CachedLibrary("procedure") InteropLibrary interopLibrary,
                                @Cached DispatchUserProcedureNode dispatchUserProcedureNode) {
        return dispatchUserProcedureNode.executeDispatch(procedure, getArgumentsWithoutLexicalScope(arguments, frame));
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
