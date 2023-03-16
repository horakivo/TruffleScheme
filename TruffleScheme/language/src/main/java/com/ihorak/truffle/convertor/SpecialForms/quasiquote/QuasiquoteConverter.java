package com.ihorak.truffle.convertor.SpecialForms.quasiquote;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.special_form.QuoteExprNode;
import com.ihorak.truffle.node.special_form.quasiquote.UnquoteSplicingInsertInfo;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;

public class QuasiquoteConverter {

    private static final int CTX_QUASIQUOTE_BODY = 1;

    private QuasiquoteConverter() {
    }

    // (quasiquote <body>)
    public static SchemeExpression convert(SchemeList quasiquoteList, ParsingContext context, ParserRuleContext quasiquoteCtx) {
        validate(quasiquoteList);
        var bodyIR = quasiquoteList.get(1);
        var bodyCtx = (ParserRuleContext) quasiquoteCtx.getChild(CTX_QUASIQUOTE_BODY);
        if (bodyIR instanceof SchemeList list) {
            var holder = convertSchemeList(list, context, bodyCtx);
        } else {
            // if the body is not a list it doesn't contain any unquote or unquote-splicing (those are converted to list)
            // therefore it behaves as Quote
            return SourceSectionUtil.setSourceSectionAndReturnExpr(new QuoteExprNode(bodyIR), bodyCtx);
        }

        return null;
    }


    private static QuasiquoteHolder convertSchemeList(SchemeList schemeList, ParsingContext context, ParserRuleContext ctx) {
        var quasiquoteHolderResult = new QuasiquoteHolder(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        SchemeCell currentCell = schemeList.list;
        ParserRuleContext currentCtx = ctx;
        SchemeCell previousCell = null;
        while (currentCell != SchemeCell.EMPTY_LIST) {
            if (currentCell.car instanceof SchemeList list) {
                if (shouldUnquoteBeDone(list, context)) {
                    handleUnquote(list, currentCell, quasiquoteHolderResult, context, ctx);
                } else if (shouldUnquoteSplicingBeDone(list, context)) {
                    quasiquoteHolderResult.unquoteSplicingToEval().add(convertDataToTruffleAST(list.get(1), context));
                    quasiquoteHolderResult.unquoteSplicingToInsert().add(new UnquoteSplicingInsertInfo(previousCell, currentCell));
                } else {
                    var isQuasiquote = isQuasiquote(list);
                    var isUnquoteOrUnquoteSplicing = isUnquote(list) || isUnquoteSplicing(list);

                    if (isQuasiquote) context.increaseQuasiquoteNestedLevel();
                    if (isUnquoteOrUnquoteSplicing) context.decreaseQuasiquoteNestedLevel();


                    //TODO validace jestli jsem v unqote or nah, pres boolean
                    var holder = convertSchemeList(list, context, ctx);
                    quasiquoteHolderResult.unquoteToEval().addAll(holder.unquoteToEval());
                    quasiquoteHolderResult.unquoteToInsert().addAll(holder.unquoteToInsert());
                    quasiquoteHolderResult.unquoteSplicingToEval().addAll(holder.unquoteSplicingToEval());
                    quasiquoteHolderResult.unquoteSplicingToInsert().addAll(holder.unquoteSplicingToInsert());


                    if (isQuasiquote) context.decreaseQuasiquoteNestedLevel();
                    if (isUnquoteOrUnquoteSplicing) context.increaseQuasiquoteNestedLevel();
                }
            }
            previousCell = currentCell;
            currentCell = currentCell.cdr;
        }

        return quasiquoteHolderResult;
    }


    private static void handleUnquote(SchemeList unquoteListIR, SchemeCell currentCell, QuasiquoteHolder holder, ParsingContext context, ParserRuleContext unquoteCtx) {
        holder.unquoteToEval().add(convertDataToTruffleAST(unquoteListIR.get(1), context));
        holder.unquoteToInsert().add(currentCell);
    }


    private static SchemeExpression convertDataToTruffleAST(Object schemeList, ParsingContext context) {
        return InternalRepresentationConverter.convert(schemeList, context, false, false);
    }


    private static boolean shouldUnquoteBeDone(SchemeList list, ParsingContext context) {
        var shouldUnquoteBeDone = isUnquote(list) && context.getQuasiquoteNestedLevel() == 0;
        if (shouldUnquoteBeDone && list.size != 2) {
            throw new SchemeException("unquote: expects exactly one expression in " + list, null);
        }

        return shouldUnquoteBeDone;
    }

    private static boolean shouldUnquoteSplicingBeDone(SchemeList list, ParsingContext context) {
        var shouldUnquoteSplicingBeDone = isUnquoteSplicing(list) && context.getQuasiquoteNestedLevel() == 0;
        if (shouldUnquoteSplicingBeDone && list.size != 2) {
            throw new SchemeException("unquote-splicing: expects exactly one expression in " + list, null);
        }

        return shouldUnquoteSplicingBeDone;
    }

    private static boolean isUnquote(SchemeList list) {
        return list.car() instanceof SchemeSymbol symbol && symbol.getValue().equals("unquote");
    }

    private static boolean isUnquoteSplicing(SchemeList list) {
        return list.car() instanceof SchemeSymbol symbol && symbol.getValue().equals("unquote-splicing");
    }

    private static boolean isQuasiquote(SchemeList list) {
        return list.car() instanceof SchemeSymbol symbol && symbol.getValue().equals("quasiquote");
    }

    private static void validate(SchemeList quasiquoteList) {
        if (quasiquoteList.size != 2) {
            throw new SchemeException("quasiquote: arity mismatch\nexpected: 1\ngiven: " + (quasiquoteList.size - 1), null);
        }
    }
}
