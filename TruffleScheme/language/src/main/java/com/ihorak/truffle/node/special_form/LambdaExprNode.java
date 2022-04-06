package com.ihorak.truffle.node.special_form;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.UserDefinedProcedure;
import com.oracle.truffle.api.frame.VirtualFrame;

public class LambdaExprNode extends SchemeExpression {

    private final UserDefinedProcedure userDefinedProcedure;


    public LambdaExprNode(UserDefinedProcedure userDefinedProcedure) {
        this.userDefinedProcedure = userDefinedProcedure;
    }

    /**
     * Parent cannot be saved only once, since the virtual frame is also containing arguments!
     * It would cause that the arguments from the previous call will be applied!
     * */
    @Override
    public UserDefinedProcedure executeFunction(VirtualFrame virtualFrame) {
        userDefinedProcedure.setParentFrame(virtualFrame.materialize());
        return userDefinedProcedure;
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        return executeFunction(virtualFrame);
    }
}
