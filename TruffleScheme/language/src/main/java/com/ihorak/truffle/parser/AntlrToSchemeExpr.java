package com.ihorak.truffle.parser;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.PrimitiveTypes.BigIntConverter;
import com.ihorak.truffle.convertor.PrimitiveTypes.BooleanConverter;
import com.ihorak.truffle.convertor.PrimitiveTypes.DoubleConverter;
import com.ihorak.truffle.convertor.PrimitiveTypes.LongConverter;
import com.ihorak.truffle.convertor.PrimitiveTypes.SchemeListConverter;
import com.ihorak.truffle.convertor.PrimitiveTypes.SchemeSymbolConverter;
import com.ihorak.truffle.convertor.PrimitiveTypes.TruffleStringConverter;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.ParserException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.literals.StringLiteralNode;

import java.math.BigInteger;

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
