package com.ihorak.truffle.parser.parser;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.literals.DoubleLiteralNode;
import com.ihorak.truffle.parser.antlr.R5RSBaseVisitor;
import com.ihorak.truffle.parser.antlr.R5RSParser;
import com.ihorak.truffle.type.*;
import org.antlr.v4.runtime.tree.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class AntlrToInternalRepresentation extends R5RSBaseVisitor<Object> {

    @Override
    public Object visitForm(R5RSParser.FormContext ctx) {
        return visit(ctx.getChild(0));
    }

    @Override
    public SchemeCell visitList(R5RSParser.ListContext ctx) {
        SchemeCell list = SchemeCell.EMPTY_LIST;
        // we are ignoring first and last child since those are '(' and ')'
        for (int i = ctx.getChildCount() - 1; i-- > 1; ) {
            list = list.cons(visit(ctx.getChild(i)), list);
        }

        return list;
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
    public String visitString(R5RSParser.StringContext ctx) {
        return ctx.STRING().getText();
    }

    @Override
    public SchemeSymbol visitSymbol(R5RSParser.SymbolContext ctx) {
        return new SchemeSymbol(ctx.SYMBOL().getText());
    }

    @Override
    public Object visitQuote(R5RSParser.QuoteContext ctx) {
        SchemeCell result = SchemeCell.EMPTY_LIST;
        // 'form -> just take second child since the first one is '
        result = result.cons(visit(ctx.getChild(1)), result);
        result = result.cons(new SchemeSymbol("quote"), result);
        return result;
    }

    @Override
    public Object visitQuasiquote(R5RSParser.QuasiquoteContext ctx) {
        SchemeCell result = SchemeCell.EMPTY_LIST;
        // 'form -> just take second child since the first one is `
        result = result.cons(visit(ctx.getChild(1)), result);
        result = result.cons(new SchemeSymbol("quasiquote"), result);
        return result;
    }

    @Override
    public Object visitUnquote(R5RSParser.UnquoteContext ctx) {
        SchemeCell result = SchemeCell.EMPTY_LIST;
        // 'form -> just take second child since the first one is ,
        result = result.cons(visit(ctx.getChild(1)), result);
        result = result.cons(new SchemeSymbol("unquote"), result);
        return result;
    }

    @Override
    public Object visitUnquote_splicing(R5RSParser.Unquote_splicingContext ctx) {
        SchemeCell result = SchemeCell.EMPTY_LIST;
        // 'form -> just take second child since the first one is ,@
        result = result.cons(visit(ctx.getChild(1)), result);
        result = result.cons(new SchemeSymbol("unquote-splicing"), result);
        return result;
    }

    @Override
    public Object visitPair(R5RSParser.PairContext ctx) {
        List<Object> arguments = cleanPairContext(ctx);
        var test = createPair(arguments);
        return test;
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
