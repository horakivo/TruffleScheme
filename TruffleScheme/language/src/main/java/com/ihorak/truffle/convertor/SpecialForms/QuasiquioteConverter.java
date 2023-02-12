package com.ihorak.truffle.convertor.SpecialForms;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.scope.WriteGlobalVariableExprNode;
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNode;
import com.ihorak.truffle.node.special_form.QuasiquoteExprNode;
import com.ihorak.truffle.node.special_form.UnquoteSplicingInsertInfo;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;

import java.util.ArrayList;
import java.util.List;

public class QuasiquioteConverter {

    private QuasiquioteConverter() {
    }


    record QuasiquoteHolder(List<SchemeExpression> unquoteToEval,
                            List<SchemeExpression> unquoteSplicingToEval,
                            List<SchemeCell> unquoteToInsert,
                            List<UnquoteSplicingInsertInfo> unquoteSplicingToInsert) {
    }

    public static QuasiquoteExprNode convert(SchemeList quasiquoteList, ParsingContext context) {
        if (quasiquoteList.size == 2) {
            var holder = quasiquoteHelper(quasiquoteList.get(1), context);
            return new QuasiquoteExprNode(quasiquoteList.get(1), holder.unquoteToEval, holder.unquoteToInsert, holder.unquoteSplicingToEval, holder.unquoteSplicingToInsert);
        } else {
            throw new SchemeException("quasiquote: arity mismatch\nexpected: 1\ngiven: " + (quasiquoteList.size - 1), null);
        }
    }

    private static QuasiquoteHolder quasiquoteHelper(Object datum, ParsingContext context) {
        if (datum instanceof SchemeList list) {
            return convertList(list, context);
//            for (Object element : list) {
//                if (element instanceof SchemeCell sublist && isUnquoteOrUnquoteSplicingList(sublist)) {
//                    if (sublist.size() == 2) {
//                        result.add(ListToExpressionConverter.convert(sublist.get(1), context));
//                    } else {
//                        throw new SchemeException("unquote: expects exactly one expression", null);
//                    }
//                }
//            }
        }

        return new QuasiquoteHolder(new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>());
    }

//    private static List<SchemeExpression> convertList(SchemeList schemeList, ParsingContext context) {
//        List<SchemeExpression> result = new ArrayList<>();
//        for (Object element : schemeList) {
//            if (element instanceof SchemeList list) {
//                if (isUnquoteOrUnquoteSplicingList(list)) {
//                    if (list.size != 2) throw new SchemeException("unquote: expects exactly one expression", null);
//                    result.add(InternalRepresentationConverter.convert(list.get(1), context, false));
//                } else {
//                    result.addAll(convertList(list, context));
//                }
//            }
//        }
//
//        return result;
//    }

    private static QuasiquoteHolder convertList(SchemeList schemeList, ParsingContext context) {
        var quasiquoteHolderResult = new QuasiquoteHolder(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        SchemeCell currentCell = schemeList.list;
        SchemeCell previousCell = null;
        while (currentCell != SchemeCell.EMPTY_LIST) {
            var element = currentCell.car;
            if (element instanceof SchemeList list) {
                if (shouldUnquoteBeDone(list, context)) {
                    if (list.size != 2) throw new SchemeException("unquote: expects exactly one expression in " + list, null);
                    quasiquoteHolderResult.unquoteToEval.add(convertDataToTruffleAST(list.get(1), context));
                    quasiquoteHolderResult.unquoteToInsert.add(currentCell);
                } else if (shouldUnquoteSplicingBeDone(list, context)) {
                    if (list.size != 2) throw new SchemeException("unquote-splicing: expects exactly one expression in " + list, null);
                    quasiquoteHolderResult.unquoteSplicingToEval.add(convertDataToTruffleAST(list.get(1), context));
                    quasiquoteHolderResult.unquoteSplicingToInsert.add(new UnquoteSplicingInsertInfo(previousCell, currentCell));
                } else {
                    var isQuasiquote = isQuasiquote(list);
                    var isUnquoteOrUnquoteSplicing = isUnquote(list) || isUnquoteSplicing(list);

                    if (isQuasiquote) context.increaseQuasiquoteNestedLevel();
                    if (isUnquoteOrUnquoteSplicing) context.decreaseQuasiquoteNestedLevel();


                    //TODO validace jestli jsem v unqote or nah, pres boolean
                    var holder = convertList(list, context);
                    quasiquoteHolderResult.unquoteToEval.addAll(holder.unquoteToEval);
                    quasiquoteHolderResult.unquoteToInsert.addAll(holder.unquoteToInsert);
                    quasiquoteHolderResult.unquoteSplicingToEval.addAll(holder.unquoteSplicingToEval);
                    quasiquoteHolderResult.unquoteSplicingToInsert.addAll(holder.unquoteSplicingToInsert);


                    if (isQuasiquote) context.decreaseQuasiquoteNestedLevel();
                    if (isUnquoteOrUnquoteSplicing) context.increaseQuasiquoteNestedLevel();
                }
            }
            previousCell = currentCell;
            currentCell = currentCell.cdr;
        }

        return quasiquoteHolderResult;
    }


    private static SchemeExpression convertDataToTruffleAST(Object schemeList, ParsingContext context) {
        var dataConvertedToAST = InternalRepresentationConverter.convert(schemeList, context, false, false);
        return dataConvertedToAST;
    }


    private static boolean shouldUnquoteBeDone(SchemeList list, ParsingContext context) {
        return isUnquote(list) && context.getQuasiquoteNestedLevel() == 0;
    }

    private static boolean shouldUnquoteSplicingBeDone(SchemeList list, ParsingContext context) {
        return isUnquoteSplicing(list) && context.getQuasiquoteNestedLevel() == 0;
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


}
