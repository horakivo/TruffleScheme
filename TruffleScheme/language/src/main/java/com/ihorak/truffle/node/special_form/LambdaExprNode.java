package com.ihorak.truffle.node.special_form;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.runtime.UserDefinedProcedure;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.frame.VirtualFrame;

public class LambdaExprNode extends SchemeExpression {


    public final RootCallTarget callTarget;
    public final int amountOfArguments;
    private final String name;
    private final boolean shouldMaterializeFrame;
    //private final boolean hasOptionalArgs;

    public LambdaExprNode(RootCallTarget callTarget, int amountOfArguments, String name, boolean shouldMaterializeFrame) {
        this.callTarget = callTarget;
        this.amountOfArguments = amountOfArguments;
        this.name = name;
        this.shouldMaterializeFrame = shouldMaterializeFrame;
    }

    @Override
    public UserDefinedProcedure executeUserDefinedProcedure(VirtualFrame virtualFrame) {
        if (shouldMaterializeFrame) {
            return new UserDefinedProcedure(callTarget, amountOfArguments, virtualFrame.materialize(), name);
        } else {
            return new UserDefinedProcedure(callTarget, amountOfArguments, null, name);
        }
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        return executeUserDefinedProcedure(virtualFrame);
    }
}
