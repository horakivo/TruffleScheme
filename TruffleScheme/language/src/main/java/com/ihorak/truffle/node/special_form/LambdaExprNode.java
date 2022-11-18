package com.ihorak.truffle.node.special_form;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.UserDefinedProcedure;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.frame.VirtualFrame;

public class LambdaExprNode extends SchemeExpression {


    private CallTarget callTarget;
    private int amountOfArguments;
    private boolean hasOptionalArgs;

    public LambdaExprNode(CallTarget callTarget, int amountOfArguments, boolean hasOptionalArgs) {
        this.callTarget = callTarget;
        this.amountOfArguments = amountOfArguments;
        this.hasOptionalArgs = hasOptionalArgs;
    }

    /**
     * Parent cannot be saved only once, since the virtual frame is also containing arguments!
     * It would cause that the arguments from the previous call will be applied!
     */
    @Override
    public UserDefinedProcedure executeUserDefinedProcedure(VirtualFrame virtualFrame) {
        return new UserDefinedProcedure(callTarget, amountOfArguments, hasOptionalArgs, virtualFrame.materialize());
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        return executeUserDefinedProcedure(virtualFrame);
    }
}
