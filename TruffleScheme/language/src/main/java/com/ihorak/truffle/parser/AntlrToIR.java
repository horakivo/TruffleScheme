package com.ihorak.truffle.parser;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemePair;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.strings.TruffleString;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class AntlrToIR extends R5RSBaseVisitor<Object> {

    public static final TruffleString.Encoding STRING_ENCODING = TruffleString.Encoding.UTF_16;

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

        return SchemeListUtil.createList(objects);

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
    public Double visitFloat(R5RSParser.FloatContext ctx) {
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
        return TruffleString.fromJavaStringUncached(trimmedString, SchemeTruffleLanguage.STRING_ENCODING);
    }

    @Override
    public SchemeSymbol visitSymbol(R5RSParser.SymbolContext ctx) {
        return new SchemeSymbol(ctx.SYMBOL().getText());
    }

    @Override
    public SchemeList visitQuote(R5RSParser.QuoteContext ctx) {
        // 'form -> just take second child since the first one is '
        return SchemeListUtil.createList(new SchemeSymbol("quote"), visit(ctx.getChild(1)));
    }

    @Override
    public SchemeList visitQuasiquote(R5RSParser.QuasiquoteContext ctx) {
        // `form -> just take second child since the first one is `
        return SchemeListUtil.createList(new SchemeSymbol("quasiquote"), visit(ctx.getChild(1)));
    }

    @Override
    public SchemeList visitUnquote(R5RSParser.UnquoteContext ctx) {
        // ,form -> just take second child since the first one is ,
        return SchemeListUtil.createList(new SchemeSymbol("unquote"), visit(ctx.getChild(1)));
    }

    @Override
    public SchemeList visitUnquote_splicing(R5RSParser.Unquote_splicingContext ctx) {
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
