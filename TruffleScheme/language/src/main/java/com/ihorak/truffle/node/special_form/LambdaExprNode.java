package com.ihorak.truffle.node.special_form;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.DispatchNodeGen;
import com.ihorak.truffle.type.SchemeSymbol;
import com.ihorak.truffle.type.UserDefinedProcedure;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.frame.VirtualFrame;

public class LambdaExprNode extends SchemeExpression {


    public final RootCallTarget callTarget;
    public final int amountOfArguments;
    private final String name;
    //private final boolean hasOptionalArgs;

    public LambdaExprNode(RootCallTarget callTarget, int amountOfArguments, String name) {
        this.callTarget = callTarget;
        this.amountOfArguments = amountOfArguments;
        this.name = name;
    }

    @Override
    public UserDefinedProcedure executeUserDefinedProcedure(VirtualFrame virtualFrame) {
        return new UserDefinedProcedure(callTarget, amountOfArguments, virtualFrame.materialize(), name);
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        return executeUserDefinedProcedure(virtualFrame);
    }
}
