package com.ihorak.truffle.convertor.SpecialForms.quasiquote;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.InterpreterException;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.special_form.QuoteExprNode;
import com.ihorak.truffle.node.special_form.quasiquote.QuasiquoteExprNode;
import com.ihorak.truffle.node.special_form.quasiquote.QuasiquoteOnlyUnquoteExprNode;
import com.ihorak.truffle.node.special_form.quasiquote.QuasiquoteOnlyUnquoteSplicingExprNode;
import com.ihorak.truffle.node.special_form.quasiquote.ReturnGivenObjectNode;
import com.ihorak.truffle.node.special_form.quasiquote.UnquoteSplicingInsertInfo;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;

public class QuasiquoteConverter {


    private QuasiquoteConverter() {
    }

    // (quasiquote <body>)
    public static SchemeExpression convert(SchemeList quasiquoteListIR, ParsingContext context, ParserRuleContext quasiquoteCtx) {
        validate(quasiquoteListIR);
        var bodyIR = quasiquoteListIR.get(1);
        //removing formContext
        var ctxBodyIndex = quasiquoteCtx.getChild(0).getText().equals("(") ? 2 : 1;
        var bodyCtx = (ParserRuleContext) quasiquoteCtx.getChild(ctxBodyIndex).getChild(0);
        if (bodyIR instanceof SchemeList listIR) {
            var holder = convertSchemeList(listIR, context, bodyCtx);
            return convertQuasiquoteHolderToSchemeExpr(holder, listIR, quasiquoteCtx);
        } else {
            // if the body is not a list it doesn't contain any unquote or unquote-splicing (those are converted to list)
            // therefore it behaves as Quote
            return SourceSectionUtil.setSourceSectionAndReturnExpr(new QuoteExprNode(bodyIR), bodyCtx);
        }
    }


    private static QuasiquoteHolder convertSchemeList(SchemeList schemeList, ParsingContext context, ParserRuleContext listCtx) {
        var quasiquoteHolderResult = new QuasiquoteHolder(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        SchemeCell currentCell = schemeList.list;
        //special case when we ,@ or , represent in the IR as (unquote/unquote-splicing ...) so we don't want to remove the '('
        //see test givenUnquoteSplicingDirectlyAfterQuasiquote_whenExecuted_thenUnquoteSplicingIsNotActioned
        int ctxIndex = listCtx.getChild(0).getText().equals("(") ? 1 : 0;
        SchemeCell previousCell = null;
        while (currentCell != SchemeCell.EMPTY_LIST) {
            if (currentCell.car instanceof SchemeList list) {
                if (shouldUnquoteBeDone(list, context)) {
                    var unquoteCtx = (ParserRuleContext) listCtx.getChild(ctxIndex).getChild(0);
                    handleUnquote(list, currentCell, quasiquoteHolderResult, context, unquoteCtx);
                } else if (shouldUnquoteSplicingBeDone(list, context)) {
                    var unquoteSplicingCtx = (ParserRuleContext) listCtx.getChild(ctxIndex).getChild(0);
                    handleUnquoteSplicing(list, quasiquoteHolderResult, previousCell, currentCell, context, unquoteSplicingCtx);
                } else {
                    // again removing form
                    var newListCtx = (ParserRuleContext) listCtx.getChild(ctxIndex).getChild(0);
                    handleList(list, quasiquoteHolderResult, context, newListCtx);
                }
            }
            previousCell = currentCell;
            currentCell = currentCell.cdr;
            ctxIndex++;
        }

        return quasiquoteHolderResult;
    }

    private static SchemeExpression convertQuasiquoteHolderToSchemeExpr(QuasiquoteHolder holder, SchemeList listIR, ParserRuleContext quasiquoteCtx) {
        if (holder.unquoteToEval().isEmpty() && holder.unquoteSplicingToEval().isEmpty()) {
            return SourceSectionUtil.setSourceSectionAndReturnExpr(new ReturnGivenObjectNode(listIR), quasiquoteCtx);
        }

        SchemeExpression resultExpr;
        if (holder.unquoteSplicingToEval().isEmpty()) {
            resultExpr = new QuasiquoteOnlyUnquoteExprNode(holder.unquoteToEval(), holder.unquoteToInsert(), listIR);
        } else if (holder.unquoteToEval().isEmpty()) {
            var isFirstPreviousCellNull = holder.unquoteSplicingToInsert().get(0).previousCell() == null;
            resultExpr = new QuasiquoteOnlyUnquoteSplicingExprNode(listIR, holder.unquoteSplicingToEval(), holder.unquoteSplicingToInsert(), isFirstPreviousCellNull);
        } else {
            var isFirstPreviousCellNull = holder.unquoteSplicingToInsert().get(0).previousCell() == null;
            resultExpr = new QuasiquoteExprNode(listIR, holder.unquoteToEval(), holder.unquoteToInsert(), holder.unquoteSplicingToEval(), holder.unquoteSplicingToInsert(), isFirstPreviousCellNull);
        }

        return SourceSectionUtil.setSourceSectionAndReturnExpr(resultExpr, quasiquoteCtx);
    }


    private static void handleUnquote(SchemeList unquoteListIR, SchemeCell currentCell, QuasiquoteHolder holder, ParsingContext context, ParserRuleContext unquoteCtx) {
        var parameterFormCtx = getUnquoteParserCtx(unquoteCtx);
        var expr = InternalRepresentationConverter.convert(unquoteListIR.get(1), context, false, false, parameterFormCtx);

        holder.unquoteToEval().add(expr);
        holder.unquoteToInsert().add(currentCell);
    }

    private static void handleUnquoteSplicing(SchemeList unquoteSplicingListIR, QuasiquoteHolder holder, SchemeCell previousCell, SchemeCell currentCell, ParsingContext context, ParserRuleContext unquoteSplicingCtx) {
        var parameterFormCtx = getUnquoteSplicingParserCtx(unquoteSplicingCtx);
        var expr = InternalRepresentationConverter.convert(unquoteSplicingListIR.get(1), context, false, false, parameterFormCtx);

        holder.unquoteSplicingToEval().add(expr);
        holder.unquoteSplicingToInsert().add(new UnquoteSplicingInsertInfo(previousCell, currentCell));
    }

    private static void handleList(SchemeList list, QuasiquoteHolder holder, ParsingContext context, ParserRuleContext listCtx) {
        var isQuasiquote = isQuasiquote(list);
        var isUnquoteOrUnquoteSplicing = isUnquote(list) || isUnquoteSplicing(list);

        if (isQuasiquote) context.increaseQuasiquoteNestedLevel();
        if (isUnquoteOrUnquoteSplicing) context.decreaseQuasiquoteNestedLevel();

        var resultHolder = convertSchemeList(list, context, listCtx);
        holder.unquoteToEval().addAll(resultHolder.unquoteToEval());
        holder.unquoteToInsert().addAll(resultHolder.unquoteToInsert());
        holder.unquoteSplicingToEval().addAll(resultHolder.unquoteSplicingToEval());
        holder.unquoteSplicingToInsert().addAll(resultHolder.unquoteSplicingToInsert());


        if (isQuasiquote) context.decreaseQuasiquoteNestedLevel();
        if (isUnquoteOrUnquoteSplicing) context.increaseQuasiquoteNestedLevel();

    }

    private static ParserRuleContext getUnquoteParserCtx(ParserRuleContext unquoteCtx) {
        if (unquoteCtx.getChild(0).getText().equals(",")) {
            // ,<expr>
            return (ParserRuleContext) unquoteCtx.getChild(1);
        } else if (unquoteCtx.getChild(0).getText().equals("(")) {
            // (unquote <expr>)
            return (ParserRuleContext) unquoteCtx.getChild(2);
        } else {
            throw InterpreterException.shouldNotReachHere();
        }
    }

    private static ParserRuleContext getUnquoteSplicingParserCtx(ParserRuleContext unquoteSplicingCtx) {
        if (unquoteSplicingCtx.getChild(0).getText().equals(",@")) {
            // ,@<expr>
            return (ParserRuleContext) unquoteSplicingCtx.getChild(1);
        } else if (unquoteSplicingCtx.getChild(0).getText().equals("(")) {
            // (unquote-splicing <expr>)
            return (ParserRuleContext) unquoteSplicingCtx.getChild(2);
        } else {
            throw InterpreterException.shouldNotReachHere();
        }
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
