package com.ihorak.truffle.node.special_form;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.DispatchNodeGen;
import com.ihorak.truffle.type.SchemeSymbol;
import com.ihorak.truffle.type.UserDefinedProcedure;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.frame.VirtualFrame;

public class LambdaExprNode extends SchemeExpression {


    public final CallTarget callTarget;
    public final boolean isTailCall;
    private final int amountOfArguments;
    private final boolean hasOptionalArgs;

    public LambdaExprNode(CallTarget callTarget, int amountOfArguments, boolean hasOptionalArgs, boolean isTailCall) {
        this.callTarget = callTarget;
        this.amountOfArguments = amountOfArguments;
        this.hasOptionalArgs = hasOptionalArgs;
        this.isTailCall = isTailCall;
    }

    @Override
    public UserDefinedProcedure executeUserDefinedProcedure(VirtualFrame virtualFrame) {
        return new UserDefinedProcedure(callTarget, amountOfArguments, hasOptionalArgs, virtualFrame.materialize());
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        return executeUserDefinedProcedure(virtualFrame);
    }
}
