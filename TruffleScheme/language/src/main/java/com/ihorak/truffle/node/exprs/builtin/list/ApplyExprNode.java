package com.ihorak.truffle.node.exprs.builtin.list;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.callable.DispatchNode;
import com.ihorak.truffle.node.callable.DispatchNodeGen;
import com.ihorak.truffle.type.AbstractProcedure;
import com.ihorak.truffle.type.PrimitiveProcedure;
import com.ihorak.truffle.type.UserDefinedProcedure;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.profiles.BranchProfile;

public abstract class ApplyExprNode extends SchemeNode {

    public abstract Object execute(MaterializedFrame frame, AbstractProcedure callable, Object[] arguments);
    @Child private DispatchNode dispatchNode = DispatchNodeGen.create();

    private final BranchProfile primitiveProcedureWrongNumberOfArgsProfile = BranchProfile.create();
    private final BranchProfile userDefinedProcedureWrongNumberOfArgsProfile = BranchProfile.create();

    @Specialization
    protected Object doApplyOnPrimitiveProcedure(MaterializedFrame frame, PrimitiveProcedure primitiveProcedure, Object[] arguments) {

        var expectedNumberOfArgs = primitiveProcedure.getNumberOfArgs();
        if (expectedNumberOfArgs != null && expectedNumberOfArgs != arguments.length) {
            primitiveProcedureWrongNumberOfArgsProfile.enter();
            throw new SchemeException(primitiveProcedure.getName() + ": arity mismatch; Expected number of arguments does not match the given number" +
                    "\nexpected: " + expectedNumberOfArgs + "" +
                    "\ngiven: " + arguments.length, this);
        }
        return dispatchNode.executeDispatch(primitiveProcedure.getCallTarget(), createArgumentsForCall(arguments, frame));
    }

    @Specialization
    protected Object doApplyOnPrimitiveProcedure(MaterializedFrame frame, UserDefinedProcedure userDefinedProcedure, Object[] arguments) {
        var expectedNumberOfArgs = userDefinedProcedure.getExpectedNumberOfArgs();
        if (expectedNumberOfArgs != arguments.length) {
            userDefinedProcedureWrongNumberOfArgsProfile.enter();
            throw new SchemeException("User defined procedure: arity mismatch; Expected number of arguments does not match the given number" +
                    "\nexpected: " + expectedNumberOfArgs + "" +
                    "\ngiven: " + arguments.length, this);
        }
        return dispatchNode.executeDispatch(userDefinedProcedure.getCallTarget(), createArgumentsForCall(arguments, userDefinedProcedure.getParentFrame().materialize()));
    }

    private Object[] createArgumentsForCall(Object[] arguments, MaterializedFrame frame) {
        Object[] result = new Object[arguments.length + 1];
        result[0] = frame;

        for (int i = 0; i < arguments.length; i++) {
            result[i + 1] = arguments[i];
        }

        return result;
    }
}