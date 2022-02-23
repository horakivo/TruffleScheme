package com.ihorak.truffle.parser;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.ProcedureCallExprNode;
import com.ihorak.truffle.parser.Util.BuiltinUtils;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeSymbol;

import java.util.ArrayList;
import java.util.List;

public class ProcedureCallConverter {

    /*
     *  --> (operand argExpr1 ... argExprN)
     * */
    //here it can be either procedure or builtin
    public static SchemeExpression convertListToProcedureCall(SchemeCell procedureList, Context context) {
        var operand = procedureList.car;
        List<SchemeExpression> arguments = getProcedureArguments((SchemeCell) procedureList.cdr, context);

        if (operand instanceof SchemeSymbol && BuiltinUtils.isBuiltinProcedure((SchemeSymbol) operand)) {
            var symbol = (SchemeSymbol) operand;
            return BuiltinConverter.createBuiltin(symbol, arguments, context);
        } else {
            return new ProcedureCallExprNode(ListToExpressionConverter.convert(operand, context), arguments);
        }
    }

    private static List<SchemeExpression> getProcedureArguments(SchemeCell argumentList, Context context) {
        List<SchemeExpression> result = new ArrayList<>();

        for (Object obj : argumentList) {
            result.add(ListToExpressionConverter.convert(obj, context));
        }
        return result;
    }
}
