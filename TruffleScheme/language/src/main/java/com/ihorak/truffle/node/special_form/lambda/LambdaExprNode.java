package com.ihorak.truffle.node.special_form.lambda;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeFunction;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;

public class LambdaExprNode extends SchemeExpression {

    private final SchemeFunction schemeFunction;


    public LambdaExprNode(SchemeFunction schemeFunction) {
        this.schemeFunction = schemeFunction;
    }

    /**
     * Parent cannot be saved only once, since the virtual frame is also containing arguments!
     * It would cause that the arguments from the previous call will be applied!
     * */
    @Override
    public SchemeFunction executeFunction(VirtualFrame virtualFrame) {
        schemeFunction.setParentFrame(virtualFrame.materialize());
        return schemeFunction;
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        return executeFunction(virtualFrame);
    }
}
