package com.ihorak.truffle.node.special_form;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import java.util.List;

public class LetExprNode extends SchemeExpression {

    //Bindings + Body expression
    //First WriteLocalVariableExpr are executed to define the bindings and then body
    @Children
    private final SchemeExpression[] bindingAndBodyExpressions;


    public LetExprNode(List<SchemeExpression> bindingAndBodyExpressions) {
        this.bindingAndBodyExpressions = bindingAndBodyExpressions.toArray(SchemeExpression[]::new);
    }


    @ExplodeLoop
    @Override
    public Object executeGeneric(final VirtualFrame virtualFrame) {
        for (int i = 0; i < bindingAndBodyExpressions.length - 1; i++) {
            bindingAndBodyExpressions[i].executeGeneric(virtualFrame);
        }

        return bindingAndBodyExpressions[bindingAndBodyExpressions.length - 1].executeGeneric(virtualFrame);
    }
}
