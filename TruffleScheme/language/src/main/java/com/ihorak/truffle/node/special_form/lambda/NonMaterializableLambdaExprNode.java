package com.ihorak.truffle.node.special_form.lambda;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.runtime.UserDefinedProcedure;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.frame.VirtualFrame;

public class NonMaterializableLambdaExprNode extends SchemeExpression {

    public final RootCallTarget callTarget;
    public final int amountOfArguments;
    private final String name;

    public NonMaterializableLambdaExprNode(RootCallTarget callTarget, int amountOfArguments, String name) {
        this.callTarget = callTarget;
        this.amountOfArguments = amountOfArguments;
        this.name = name;
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        return new UserDefinedProcedure(amountOfArguments, callTarget, null, name);
    }
}
