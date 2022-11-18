package com.ihorak.truffle.node.macro;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.special_form.LambdaExprNode;
import com.ihorak.truffle.type.SchemeMacro;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.frame.VirtualFrame;

public class DefineMacroExprNode extends SchemeExpression {
    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private LambdaExprNode transformationProcedure;

    public DefineMacroExprNode(LambdaExprNode transformationProcedure) {
        this.transformationProcedure = transformationProcedure;
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        var transformationProc = transformationProcedure.executeUserDefinedProcedure(virtualFrame);
        return new SchemeMacro(transformationProc);
    }
}
