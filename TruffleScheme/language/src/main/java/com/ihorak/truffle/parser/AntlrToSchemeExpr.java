package com.ihorak.truffle.parser;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeExpression;

public class AntlrToSchemeExpr extends R5RSBaseVisitor<SchemeExpression> {

    private final ParsingContext globalParsingContext;
    private final AntlrToIR antlrToIR = new AntlrToIR();


    public AntlrToSchemeExpr(ParsingContext globalParsingContext) {
        this.globalParsingContext = globalParsingContext;
    }

    @Override
    public SchemeExpression visitForm(R5RSParser.FormContext ctx) {
        var ir = antlrToIR.visit(ctx.getChild(0));
        var expr = InternalRepresentationConverter.convert(ir, globalParsingContext, false, true, ctx);

        return expr;
    }

}
