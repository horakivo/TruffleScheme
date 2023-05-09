package com.ihorak.truffle.parser;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.context.ConverterContext;
import com.ihorak.truffle.node.SchemeExpression;

public class AntlrToSchemeExpr extends R5RSBaseVisitor<SchemeExpression> {

    private final ConverterContext globalConverterContext;
    private final AntlrToIR antlrToIR = new AntlrToIR();


    public AntlrToSchemeExpr(ConverterContext globalConverterContext) {
        this.globalConverterContext = globalConverterContext;
    }

    @Override
    public SchemeExpression visitForm(R5RSParser.FormContext ctx) {
        var ir = antlrToIR.visit(ctx.getChild(0));
        var expr = InternalRepresentationConverter.convert(ir, globalConverterContext, false, true, ctx);

        return expr;
    }

}
