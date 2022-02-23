package com.ihorak.truffle.node.special_form.lambda;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeFunction;
import com.oracle.truffle.api.frame.VirtualFrame;

//TODO ask when to use abstract class with specialization or when just this pure class
public class LambdaExprNode extends SchemeExpression {

    private final SchemeFunction schemeFunction;

    public LambdaExprNode(SchemeFunction schemeFunction) {
        this.schemeFunction = schemeFunction;
    }

    @Override
    public SchemeFunction executeFunction(VirtualFrame virtualFrame) {
        return schemeFunction;
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        return schemeFunction;
    }
}
