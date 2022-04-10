package com.ihorak.truffle.node.macro;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.special_form.LambdaExprNode;
import com.ihorak.truffle.type.SchemeMacro;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

public class DefineMacroExprNode extends SchemeExpression {

    private final SchemeSymbol name;
    @Child
    private LambdaExprNode transformationProcedure;

    public DefineMacroExprNode(SchemeSymbol name, LambdaExprNode transformationProcedure) {
        this.name = name;
        this.transformationProcedure = transformationProcedure;
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        var transformationProc = transformationProcedure.executeUserDefinedProcedure(virtualFrame);
        return new SchemeMacro(transformationProc);
    }

    public SchemeSymbol getName() {
        return name;
    }

    public LambdaExprNode getTransformationProcedure() {
        return transformationProcedure;
    }
}
