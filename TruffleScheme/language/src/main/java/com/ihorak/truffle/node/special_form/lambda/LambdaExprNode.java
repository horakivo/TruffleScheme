package com.ihorak.truffle.node.special_form.lambda;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeFunction;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;

public class LambdaExprNode extends SchemeExpression {

    private final SchemeFunction schemeFunction;

//    @CompilerDirectives.CompilationFinal
//    private boolean isScopeSet = false;


    public LambdaExprNode(SchemeFunction schemeFunction) {
        this.schemeFunction = schemeFunction;
    }

    @Override
    public SchemeFunction executeFunction(VirtualFrame virtualFrame) {
//        if (!isScopeSet) {
//            CompilerDirectives.transferToInterpreterAndInvalidate();
//            schemeFunction.setParentFrame(virtualFrame.materialize());
//            isScopeSet = true;
//        }
        schemeFunction.setParentFrame(virtualFrame.materialize());
        return schemeFunction;
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        return executeFunction(virtualFrame);
    }
}
