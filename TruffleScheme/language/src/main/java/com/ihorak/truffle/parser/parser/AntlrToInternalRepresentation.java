package com.ihorak.truffle.parser.parser;

import com.ihorak.truffle.parser.antlr.R5RSBaseVisitor;
import com.ihorak.truffle.parser.antlr.R5RSParser;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemePair;
import com.ihorak.truffle.type.SchemeSymbol;
import org.antlr.v4.runtime.tree.TerminalNode;
import com.oracle.truffle.api.strings.TruffleString;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class AntlrToInternalRepresentation extends R5RSBaseVisitor<Object> {

    private static final TruffleString.Encoding STRING_ENCODING = TruffleString.Encoding.UTF_16;

    @Override
    public Object visitForm(R5RSParser.FormContext ctx) {
        return visit(ctx.getChild(0));
    }

    @Override
    public SchemeList visitList(R5RSParser.ListContext ctx) {
        Object[] objects = new Object[ctx.getChildCount() - 2];
        var index = 0;
        // we are ignoring first and last child since those are '(' and ')'
        for (int i = 1; i < ctx.getChildCount() - 1; i++) {
            objects[index] = visit(ctx.getChild(i));
            index++;
        }

        var schemeList = SchemeListUtil.createList(objects);
        var startCharIndex = ctx.start.getStartIndex();
        var stopCharIndex = ctx.stop.getStopIndex();
        schemeList.setSourceSectionInfo(startCharIndex, stopCharIndex - startCharIndex);
        return schemeList;
    }

    @Override
    public Object visitNumber(R5RSParser.NumberContext ctx) {
        try {
            return Long.parseLong(ctx.NUMBER().getText());
        } catch (NumberFormatException e) {
            return new BigInteger(ctx.NUMBER().getText());
        }
    }

    @Override
    public Object visitFloat(R5RSParser.FloatContext ctx) {
        return Double.parseDouble(ctx.FLOAT().getText());
    }

    @Override
    public Boolean visitBoolean(R5RSParser.BooleanContext ctx) {
        var booleanText = ctx.BOOLEAN().getText();
        return booleanText.equals("#t") || booleanText.equals("#T");
    }

    @Override
    public TruffleString visitString(R5RSParser.StringContext ctx) {
        var stringWithDoubleQuotes = ctx.STRING().getText();
        var trimmedString = stringWithDoubleQuotes.substring(1, stringWithDoubleQuotes.length() - 1);
        return TruffleString.fromJavaStringUncached(trimmedString, STRING_ENCODING);
    }

    @Override
    public SchemeSymbol visitSymbol(R5RSParser.SymbolContext ctx) {
        return new SchemeSymbol(ctx.SYMBOL().getText());
    }

    @Override
    public Object visitQuote(R5RSParser.QuoteContext ctx) {
        // 'form -> just take second child since the first one is '
        return SchemeListUtil.createList(new SchemeSymbol("quote"), visit(ctx.getChild(1)));
    }

    @Override
    public Object visitQuasiquote(R5RSParser.QuasiquoteContext ctx) {
        // `form -> just take second child since the first one is `
        return SchemeListUtil.createList(new SchemeSymbol("quasiquote"), visit(ctx.getChild(1)));
    }

    @Override
    public Object visitUnquote(R5RSParser.UnquoteContext ctx) {
        // ,form -> just take second child since the first one is ,
        return SchemeListUtil.createList(new SchemeSymbol("unquote"), visit(ctx.getChild(1)));
    }

    @Override
    public Object visitUnquote_splicing(R5RSParser.Unquote_splicingContext ctx) {
        // ,@form -> just take second child since the first one is ,@
        return SchemeListUtil.createList(new SchemeSymbol("unquote-splicing"), visit(ctx.getChild(1)));
    }

    @Override
    public Object visitPair(R5RSParser.PairContext ctx) {
        List<Object> arguments = cleanPairContext(ctx);
        return createPair(arguments);
    }

    private List<Object> cleanPairContext(R5RSParser.PairContext ctx) {
        List<Object> arguments = new ArrayList<>();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            var child = ctx.getChild(i);
            if (child instanceof TerminalNode) {
                continue;
            }
            arguments.add(visit(child));
        }

        return arguments;
    }

    private SchemePair createPair(List<Object> arguments) {
        if (arguments.size() > 2) {
            return new SchemePair(arguments.remove(0), createPair(arguments));
        } else {
            return new SchemePair(arguments.get(0), arguments.get(1));
        }
    }
}
