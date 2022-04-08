package com.ihorak.truffle.node.exprs.builtin.list;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.callable.DispatchNode;
import com.ihorak.truffle.node.callable.DispatchNodeGen;
import com.ihorak.truffle.type.AbstractProcedure;
import com.ihorak.truffle.type.PrimitiveProcedure;
import com.ihorak.truffle.type.UserDefinedProcedure;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.profiles.BranchProfile;

public abstract class ApplyExprNode extends SchemeNode {

    public abstract Object execute(AbstractProcedure callable, Object[] arguments);
    @Child private DispatchNode dispatchNode = DispatchNodeGen.create();

    private final BranchProfile primitiveProcedureWrongNumberOfArgsProfile = BranchProfile.create();
    private final BranchProfile userDefinedProcedureWrongNumberOfArgsProfile = BranchProfile.create();

    @Specialization
    protected Object doApplyOnPrimitiveProcedure(PrimitiveProcedure primitiveProcedure, Object[] arguments) {

        var expectedNumberOfArgs = primitiveProcedure.getNumberOfArgs();
        if (expectedNumberOfArgs != null && expectedNumberOfArgs != arguments.length) {
            primitiveProcedureWrongNumberOfArgsProfile.enter();
            throw new SchemeException(primitiveProcedure.getName() + ": arity mismatch; Expected number of arguments does not match the given number" +
                    "\nexpected: " + expectedNumberOfArgs + "" +
                    "\ngiven: " + arguments.length, this);
        }
        return dispatchNode.executeDispatch(primitiveProcedure.getCallTarget(), arguments);
    }

    @Specialization
    protected Object doApplyOnPrimitiveProcedure(UserDefinedProcedure userDefinedProcedure, Object[] arguments) {
        var expectedNumberOfArgs = userDefinedProcedure.getExpectedNumberOfArgs();
        if (expectedNumberOfArgs != arguments.length) {
            userDefinedProcedureWrongNumberOfArgsProfile.enter();
            throw new SchemeException("User defined procedure: arity mismatch; Expected number of arguments does not match the given number" +
                    "\nexpected: " + expectedNumberOfArgs + "" +
                    "\ngiven: " + arguments.length, this);
        }
        return dispatchNode.executeDispatch(userDefinedProcedure.getCallTarget(), arguments);
    }
}