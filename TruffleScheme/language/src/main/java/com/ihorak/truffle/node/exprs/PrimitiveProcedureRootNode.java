package com.ihorak.truffle.node.exprs;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;


public class PrimitiveProcedureRootNode extends RootNode {

    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private SchemeExpression expression;

    public PrimitiveProcedureRootNode(TruffleLanguage<?> language, SchemeExpression expression) {
        super(language, null);
        this.expression = expression;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return expression.executeGeneric(frame);
    }
}
