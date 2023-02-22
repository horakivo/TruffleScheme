package com.ihorak.truffle.parser;

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
        return visit(ctx.getChild(0));
    }

    @Override
    public SchemeExpression visitList(R5RSParser.ListContext ctx) {
        var ir = antlrToIR.visitList(ctx);
        return SchemeListConverter.convert(ir, globalParsingContext, false, true, ctx.start, ctx.stop);
    }

    @Override
    public SchemeExpression visitNumber(R5RSParser.NumberContext ctx) {
        var ir = antlrToIR.visitNumber(ctx);
        var token = ctx.NUMBER().getSymbol();
        if (ir instanceof Long longNumber) {
            return LongConverter.convert(longNumber, token);
        } else if (ir instanceof BigInteger bigIntNumber) {
            return BigIntConverter.convert(bigIntNumber, token);
        } else {
            throw new ParserException("Unsupported number during parsing");
        }
    }

    @Override
    public SchemeExpression visitFloat(R5RSParser.FloatContext ctx) {
        var ir = antlrToIR.visitFloat(ctx);
        var token = ctx.FLOAT().getSymbol();
        return DoubleConverter.convert(ir, token);
    }

    @Override
    public SchemeExpression visitBoolean(R5RSParser.BooleanContext ctx) {
        var ir = antlrToIR.visitBoolean(ctx);
        var token = ctx.BOOLEAN().getSymbol();

        return BooleanConverter.convert(ir, token);
    }

    @Override
    public StringLiteralNode visitString(R5RSParser.StringContext ctx) {
        var ir = antlrToIR.visitString(ctx);
        var token = ctx.STRING().getSymbol();
        return TruffleStringConverter.convert(ir, token);
    }

    @Override
    public SchemeExpression visitSymbol(R5RSParser.SymbolContext ctx) {
        var ir = antlrToIR.visitSymbol(ctx);
        var token = ctx.SYMBOL().getSymbol();
        return SchemeSymbolConverter.convert(ir, globalParsingContext, token);
    }

    @Override
    public SchemeExpression visitQuote(R5RSParser.QuoteContext ctx) {
        var ir = antlrToIR.visitQuote(ctx);
        //TODO handle here the startIndex and lenght correctly!!
        //every 'datum is converted to (quote datum)
        return SchemeListConverter.convert(ir, globalParsingContext, false, true, ctx.start, ctx.stop);
    }

    @Override
    public SchemeExpression visitQuasiquote(R5RSParser.QuasiquoteContext ctx) {
        var ir = antlrToIR.visitQuasiquote(ctx);
        //TODO handle here the startIndex and lenght correctly!!
        return SchemeListConverter.convert(ir, globalParsingContext, false, true, ctx.start, ctx.stop);
    }

    @Override
    public SchemeExpression visitUnquote(R5RSParser.UnquoteContext ctx) {
        var ir = antlrToIR.visitUnquote(ctx);
        //TODO handle here the startIndex and lenght correctly!!
        return SchemeListConverter.convert(ir, globalParsingContext, false, true, ctx.start, ctx.stop);
    }

    @Override
    public SchemeExpression visitUnquote_splicing(R5RSParser.Unquote_splicingContext ctx) {
        var ir = antlrToIR.visitUnquote_splicing(ctx);
        //TODO handle here the startIndex and lenght correctly!!
        return SchemeListConverter.convert(ir, globalParsingContext, false, true, ctx.start, ctx.stop);
    }

    @Override
    public SchemeExpression visitPair(R5RSParser.PairContext ctx) {
        throw new ParserException("should not reach here");
    }

}
