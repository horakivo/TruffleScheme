package com.ihorak.truffle.node.special_form;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.context.Mode;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.context.Context;
import com.ihorak.truffle.parser.ListToExpressionConverter;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.frame.VirtualFrame;

import java.util.ArrayList;
import java.util.List;

public class QuasiquoteExprNode extends SchemeExpression {

    private final Object datum;

    public QuasiquoteExprNode(Object datum) {
        this.datum = datum;
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        if (datum instanceof Long || datum instanceof Boolean || datum instanceof SchemeSymbol) {
            return datum;
        } else if (datum instanceof SchemeCell) {
            return convertList((SchemeCell) datum, false, virtualFrame);
        }
        throw new SchemeException("Unsupported data type. Type: " + datum);
    }

    private Object convertList(SchemeCell schemeCell, boolean isUnquoteOrUnquoteSplicingContext, VirtualFrame virtualFrame) {
        List<Object> currentList = new ArrayList<>();
        boolean isCurrentContextUnquoteOrUnquoteSplicing = isUnquoteOrUnquoteSplicingContext || isUnquote(schemeCell) || isUnquoteSplicing(schemeCell);

        for (Object obj : schemeCell) {
            if (obj instanceof SchemeCell) {
                var list = (SchemeCell) obj;
                var result = convertList(list, isCurrentContextUnquoteOrUnquoteSplicing, virtualFrame);
                if (result instanceof List) {
                    currentList.addAll((List<?>) result);
                } else {
                    currentList.add(result);
                }
            } else {
                currentList.add(obj);
            }
        }

        var list = convertListToSchemeList(currentList);
        if (isUnquote((list))) {
            return handleUnquote(list, isUnquoteOrUnquoteSplicingContext, virtualFrame);
        } else if (isUnquoteSplicing(list)) {
            return handleUnquoteSplicing(list, isUnquoteOrUnquoteSplicingContext, virtualFrame);
        }
        return list;
    }


    private SchemeCell convertListToSchemeList(List<Object> list) {
        SchemeCell result = SchemeCell.EMPTY_LIST;
        for (int i = list.size(); i-- > 0; ) {
            result = result.cons(list.get(i), result);
        }

        return result;
    }


    private boolean isUnquote(SchemeCell schemeCell) {
        var firstElement = schemeCell.car;
        return firstElement instanceof SchemeSymbol && ((SchemeSymbol) firstElement).getValue().equals("unquote");
    }

    private boolean isUnquoteSplicing(SchemeCell schemeCell) {
        var firstElement = schemeCell.car;
        return firstElement instanceof SchemeSymbol && ((SchemeSymbol) firstElement).getValue().equals("unquote-splicing");
    }

    private boolean isDefineExpression(SchemeCell schemeCell) {
        var firstElement = schemeCell.car;
        return firstElement instanceof SchemeSymbol && ((SchemeSymbol) firstElement).getValue().equals("define");
    }


    private Object handleUnquote(SchemeCell unquoteList, boolean isUnquoteOrUnquoteSplicingContext, VirtualFrame frame) {
        if (unquoteList.size() == 2) {
            if (isUnquoteOrUnquoteSplicingContext) {
                throw new SchemeException("unquote or unquote-splicing: not in quasiquote in: " + unquoteList);
            }
            var valueToBeEvaluated = unquoteList.get(1);
            if (valueToBeEvaluated instanceof SchemeCell && isDefineExpression((SchemeCell) valueToBeEvaluated)) {
                throw new SchemeException("define: not allowed in an expression context in: " + valueToBeEvaluated);
            }
            return ListToExpressionConverter.convert(valueToBeEvaluated, createRuntimeContext()).executeGeneric(frame);
        }
        throw new SchemeException("unquote: expects exactly one expression");
    }

    private List<Object> handleUnquoteSplicing(SchemeCell unquoteSplicingList, boolean isUnquoteOrUnquoteSplicingContext, VirtualFrame frame) {
        if (unquoteSplicingList.size() == 2) {
            var evaluated = handleUnquote(unquoteSplicingList, isUnquoteOrUnquoteSplicingContext, frame);

            if (evaluated instanceof SchemeCell) {
                List<Object> result = new ArrayList<>();
                for (Object obj : (SchemeCell) evaluated) {
                    result.add(obj);
                }
                return result;
            }
            throw new SchemeException("unquote-splicing: contract violation \n expected: list? \n given: " + evaluated);
        } else {
            throw new SchemeException("unquote-splicing: expects exactly one expression");
        }
    }

    private Context createRuntimeContext() {
        var context = new Context();
        context.setMode(Mode.RUN_TIME);

        return context;
    }
}
