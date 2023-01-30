package com.ihorak.truffle.node.special_form;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import java.util.List;

public class QuasiquoteExprNode extends SchemeExpression {

    private final Object datum;
    @Children
    private final SchemeExpression[] unquoteToEval;
    @Children
    private final SchemeExpression[] unquoteSplicingToEval;

    private final SchemeCell[] unquoteToInsert;
    private final SchemeCell[] unquoteSplicingToInsert;

    public QuasiquoteExprNode(Object datum, List<SchemeExpression> unquoteToEval, List<SchemeCell> unquoteToInsert, List<SchemeExpression> unquoteSplicingToEval, List<SchemeCell> unquoteSplicingToInsert) {
        this.datum = datum;
        this.unquoteToEval = unquoteToEval.toArray(SchemeExpression[]::new);
        this.unquoteToInsert = unquoteToInsert.toArray(SchemeCell[]::new);
        this.unquoteSplicingToEval = unquoteSplicingToEval.toArray(SchemeExpression[]::new);
        this.unquoteSplicingToInsert = unquoteSplicingToInsert.toArray(SchemeCell[]::new);
    }


    @Override
    @ExplodeLoop
    public Object executeGeneric(final VirtualFrame frame) {
        for (int i = 0; i < unquoteToEval.length; i++) {
            var test = unquoteToEval[i].executeGeneric(frame);
            unquoteToInsert[i].car = test;
        }

        for (int i = 0; i < unquoteSplicingToEval.length; i++) {
            var test = (SchemeList) unquoteSplicingToEval[i].executeGeneric(frame);
            //todo validate whether it returns list
            var previousCell = unquoteSplicingToInsert[i + 1];
            var cellTeReplace = unquoteSplicingToInsert[i];
            previousCell.cdr = test.list;
            test.bindingCell.cdr = cellTeReplace.cdr;

        }

        return datum;
    }

//    @Override
//    public Object executeGeneric(final VirtualFrame virtualFrame) {
//        if (toBeEvaluated.length == 0) return datum;
//
//        var result = SchemeCell.EMPTY_LIST;
//        int index = 0;
//        if (datum instanceof SchemeCell list) {
//            for (int i = list.size() - 1; i >= 0; i--) {
//                var obj = list.get(i);
//                if (shouldBeReplaced(obj)) {
//                    result = new SchemeCell(toBeEvaluated[index].executeGeneric(virtualFrame), result);
//                    index++;
//                } else {
//                    result = new SchemeCell(obj, result);
//                }
//            }
//        }
//
//        return result;
//
//    }

//    private boolean shouldBeReplaced(Object obj) {
//        if (obj instanceof SchemeCell list) {
//            var firstElement = list.car;
//            return firstElement instanceof SchemeSymbol symbol && (symbol.getValue().equals("unquote") || symbol.getValue()
//                                                                                                                .equals("unquote-splicing"));
//        }
//
//        return false;
//    }

//    @Override
//    public Object executeGeneric(VirtualFrame virtualFrame) {
//        if (datum instanceof Long || datum instanceof Boolean || datum instanceof SchemeSymbol) {
//            return datum;
//        } else if (datum instanceof SchemeCell list) {
//            return convertList(list, false, virtualFrame);
//        }
//        throw new SchemeException("Unsupported data type. Type: " + datum, this);
//    }
//
//    private Object convertList(SchemeCell schemeCell, boolean isUnquoteOrUnquoteSplicingContext, VirtualFrame virtualFrame) {
//        List<Object> currentList = new ArrayList<>();
//        boolean isCurrentContextUnquoteOrUnquoteSplicing = isUnquoteOrUnquoteSplicingContext || isUnquote(schemeCell) || isUnquoteSplicing(schemeCell);
//
//        for (Object obj : schemeCell) {
//            if (obj instanceof SchemeCell list) {
//                var result = convertList(list, isCurrentContextUnquoteOrUnquoteSplicing, virtualFrame);
//                if (result instanceof List) {
//                    currentList.addAll((List<?>) result);
//                } else {
//                    currentList.add(result);
//                }
//            } else {
//                currentList.add(obj);
//            }
//        }
//
//        var list = convertListToSchemeList(currentList);
//        if (isUnquote((list))) {
//            return handleUnquote(list, isUnquoteOrUnquoteSplicingContext, virtualFrame);
//        } else if (isUnquoteSplicing(list)) {
//            return handleUnquoteSplicing(list, isUnquoteOrUnquoteSplicingContext, virtualFrame);
//        }
//        return list;
//    }
//
//
//    private SchemeCell convertListToSchemeList(List<Object> list) {
//        SchemeCell result = SchemeCell.EMPTY_LIST;
//        for (int i = list.size(); i-- > 0; ) {
//            result = result.cons(list.get(i), result);
//        }
//
//        return result;
//    }
//
//
//    private boolean isUnquote(SchemeCell schemeCell) {
//        var firstElement = schemeCell.car;
//        return firstElement instanceof SchemeSymbol && ((SchemeSymbol) firstElement).getValue().equals("unquote");
//    }
//
//    private boolean isUnquoteSplicing(SchemeCell schemeCell) {
//        var firstElement = schemeCell.car;
//        return firstElement instanceof SchemeSymbol && ((SchemeSymbol) firstElement).getValue().equals("unquote-splicing");
//    }
//
//    private boolean isDefineExpression(SchemeCell schemeCell) {
//        var firstElement = schemeCell.car;
//        return firstElement instanceof SchemeSymbol && ((SchemeSymbol) firstElement).getValue().equals("define");
//    }
//
//
//    private Object handleUnquote(SchemeCell unquoteList, boolean isUnquoteOrUnquoteSplicingContext, VirtualFrame frame) {
//        if (unquoteList.size() == 2) {
//            if (isUnquoteOrUnquoteSplicingContext) {
//                throw new SchemeException("unquote or unquote-splicing: not in quasiquote in: " + unquoteList, this);
//            }
//            var valueToBeEvaluated = unquoteList.get(1);
//            if (valueToBeEvaluated instanceof SchemeCell && isDefineExpression((SchemeCell) valueToBeEvaluated)) {
//                throw new SchemeException("define: not allowed in an expression context in: " + valueToBeEvaluated, this);
//            }
//            return ListToExpressionConverter.convert(valueToBeEvaluated, parsingContext).executeGeneric(frame);
//        }
//        throw new SchemeException("unquote: expects exactly one expression", this);
//    }
//
//    private List<Object> handleUnquoteSplicing(SchemeCell unquoteSplicingList, boolean isUnquoteOrUnquoteSplicingContext, VirtualFrame frame) {
//        if (unquoteSplicingList.size() == 2) {
//            var evaluated = handleUnquote(unquoteSplicingList, isUnquoteOrUnquoteSplicingContext, frame);
//
//            if (evaluated instanceof SchemeCell) {
//                List<Object> result = new ArrayList<>();
//                for (Object obj : (SchemeCell) evaluated) {
//                    result.add(obj);
//                }
//                return result;
//            }
//            throw new SchemeException("unquote-splicing: contract violation \n expected: list? \n given: " + evaluated, this);
//        } else {
//            throw new SchemeException("unquote-splicing: expects exactly one expression", this);
//        }
//    }
}
