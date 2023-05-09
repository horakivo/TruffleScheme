package com.ihorak.truffle.convertor.special_form.quasiquote;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.ConverterContext;
import com.ihorak.truffle.exceptions.InterpreterException;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.special_form.QuoteExprNode;
import com.ihorak.truffle.node.special_form.quasiquote.QuasiquoteExprNode;
import com.ihorak.truffle.node.special_form.quasiquote.QuasiquoteOnlyUnquoteExprNode;
import com.ihorak.truffle.node.special_form.quasiquote.QuasiquoteOnlyUnquoteSplicingExprNode;
import com.ihorak.truffle.node.special_form.quasiquote.ReturnGivenObjectNode;
import com.ihorak.truffle.node.special_form.quasiquote.UnquoteSplicingInsertInfo;
import com.ihorak.truffle.runtime.SchemeList;
import com.ihorak.truffle.runtime.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class QuasiquoteConverter {


    private QuasiquoteConverter() {
    }

    // (quasiquote <body>)
    public static SchemeExpression convert(SchemeList quasiquoteListIR, ConverterContext context, @Nullable ParserRuleContext quasiquoteCtx) {
        validate(quasiquoteListIR);
        var bodyIR = quasiquoteListIR.get(1);
        var bodyCtx = getQuasiquoteBodyCtx(quasiquoteCtx);
        if (bodyIR instanceof SchemeList listIR) {
            var clonedListIR = listIR.shallowClone();
            var holder = convertSchemeList(clonedListIR, context, bodyCtx);
            return convertQuasiquoteHolderToSchemeExpr(holder, clonedListIR, quasiquoteCtx);
        } else {
            // if the body is not a list it doesn't contain any unquote or unquote-splicing (those are converted to list)
            // therefore it behaves as Quote
            return SourceSectionUtil.setSourceSectionAndReturnExpr(new QuoteExprNode(bodyIR), bodyCtx);
        }
    }

    private static ParserRuleContext getQuasiquoteBodyCtx(@Nullable ParserRuleContext quasiquoteCtx) {
        if (quasiquoteCtx == null) return null;

        var ctxBodyIndex = quasiquoteCtx.getChild(0).getText().equals("(") ? 2 : 1;
        return (ParserRuleContext) quasiquoteCtx.getChild(ctxBodyIndex).getChild(0);
    }


    private static QuasiquoteHolder convertSchemeList(SchemeList schemeList, ConverterContext context, @Nullable ParserRuleContext listCtx) {
        var quasiquoteHolderResult = new QuasiquoteHolder(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        SchemeList currentList = schemeList;
        //special case when we ,@ or , represent in the IR as (unquote/unquote-splicing ...) so we don't want to remove the '('
        //see test givenUnquoteSplicingDirectlyAfterQuasiquote_whenExecuted_thenUnquoteSplicingIsNotActioned
        int ctxIndex = listCtx != null ? (listCtx.getChild(0).getText().equals("(") ? 1 : 0) : Integer.MIN_VALUE;
        SchemeList previousCell = null;
        while (currentList != SchemeList.EMPTY_LIST) {
            if (currentList.car instanceof SchemeList list) {
                if (shouldUnquoteBeDone(list, context)) {
                    var unquoteCtx = listCtx != null ? (ParserRuleContext) listCtx.getChild(ctxIndex).getChild(0) : null;
                    handleUnquote(list, currentList, quasiquoteHolderResult, context, unquoteCtx);
                } else if (shouldUnquoteSplicingBeDone(list, context)) {
                    var unquoteSplicingCtx = listCtx != null ? (ParserRuleContext) listCtx.getChild(ctxIndex).getChild(0) : null;
                    handleUnquoteSplicing(list, quasiquoteHolderResult, previousCell, currentList, context, unquoteSplicingCtx);
                } else {
                    // again removing form
                    var newListCtx = listCtx != null ? (ParserRuleContext) listCtx.getChild(ctxIndex).getChild(0) : null;
                    handleList(list, quasiquoteHolderResult, context, newListCtx);
                }
            }
            previousCell = currentList;
            currentList = currentList.cdr;
            ctxIndex++;
        }

        return quasiquoteHolderResult;
    }

    private static SchemeExpression convertQuasiquoteHolderToSchemeExpr(QuasiquoteHolder holder, SchemeList listIR, @Nullable ParserRuleContext quasiquoteCtx) {
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


    private static void handleUnquote(SchemeList unquoteListIR, SchemeList currentCell, QuasiquoteHolder holder, ConverterContext context, @Nullable ParserRuleContext unquoteCtx) {
        var parameterFormCtx = getUnquoteParserCtx(unquoteCtx);
        var expr = InternalRepresentationConverter.convert(unquoteListIR.get(1), context, false, false, parameterFormCtx);

        holder.unquoteToEval().add(expr);
        holder.unquoteToInsert().add(currentCell);
    }

    private static void handleUnquoteSplicing(SchemeList unquoteSplicingListIR, QuasiquoteHolder holder, SchemeList previousCell, SchemeList currentCell, ConverterContext context, @Nullable ParserRuleContext unquoteSplicingCtx) {
        var parameterFormCtx = getUnquoteSplicingParserCtx(unquoteSplicingCtx);
        var expr = InternalRepresentationConverter.convert(unquoteSplicingListIR.get(1), context, false, false, parameterFormCtx);

        holder.unquoteSplicingToEval().add(expr);
        holder.unquoteSplicingToInsert().add(new UnquoteSplicingInsertInfo(previousCell, currentCell));
    }

    private static void handleList(SchemeList list, QuasiquoteHolder holder, ConverterContext context, @Nullable ParserRuleContext listCtx) {
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

    @Nullable
    private static ParserRuleContext getUnquoteParserCtx(@Nullable ParserRuleContext unquoteCtx) {
        if (unquoteCtx == null) return null;

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

    @Nullable
    private static ParserRuleContext getUnquoteSplicingParserCtx(@Nullable ParserRuleContext unquoteSplicingCtx) {
        if (unquoteSplicingCtx == null) return null;

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

    private static boolean shouldUnquoteBeDone(SchemeList list, ConverterContext context) {
        var shouldUnquoteBeDone = isUnquote(list) && context.getQuasiquoteNestedLevel() == 0;
        if (shouldUnquoteBeDone && list.size != 2) {
            throw new SchemeException("unquote: expects exactly one expression in " + list, null);
        }

        return shouldUnquoteBeDone;
    }

    private static boolean shouldUnquoteSplicingBeDone(SchemeList list, ConverterContext context) {
        var shouldUnquoteSplicingBeDone = isUnquoteSplicing(list) && context.getQuasiquoteNestedLevel() == 0;
        if (shouldUnquoteSplicingBeDone && list.size != 2) {
            throw new SchemeException("unquote-splicing: expects exactly one expression in " + list, null);
        }

        return shouldUnquoteSplicingBeDone;
    }

    private static boolean isUnquote(SchemeList list) {
        return list.car instanceof SchemeSymbol symbol && symbol.value().equals("unquote");
    }

    private static boolean isUnquoteSplicing(SchemeList list) {
        return list.car instanceof SchemeSymbol symbol && symbol.value().equals("unquote-splicing");
    }

    private static boolean isQuasiquote(SchemeList list) {
        return list.car instanceof SchemeSymbol symbol && symbol.value().equals("quasiquote");
    }

    private static void validate(SchemeList quasiquoteList) {
        if (quasiquoteList.size != 2) {
            throw new SchemeException("quasiquote: arity mismatch\nexpected: 1\ngiven: " + (quasiquoteList.size - 1), null);
        }
    }
}
