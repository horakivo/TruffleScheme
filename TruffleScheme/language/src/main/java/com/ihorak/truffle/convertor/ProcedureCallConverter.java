package com.ihorak.truffle.convertor;

import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.util.BuiltinUtils;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.CallableExprNodeGen;
import com.ihorak.truffle.node.callable.MacroCallableExprNode;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeSymbol;

import java.util.ArrayList;
import java.util.List;

public class ProcedureCallConverter {

    /*
     *  --> (operand argExpr1 ... argExprN)
     * */
    //here it can be either procedure or builtin
    public static SchemeExpression convertListToProcedureCall(SchemeCell procedureList, ParsingContext context) {
        var operand = procedureList.car;
        List<SchemeExpression> arguments = getProcedureArguments(procedureList.cdr, context);


        if (operand instanceof SchemeSymbol schemeSymbol) {
            if (BuiltinUtils.isBuiltinProcedure(schemeSymbol)) {
                return BuiltinConverter.createBuiltin(schemeSymbol, arguments, context);
            } else if (context.isMacro(schemeSymbol)) {
                List<Object> notEvaluatedArgs = new ArrayList<>();
                procedureList.cdr.forEach(notEvaluatedArgs::add);
                var macroExpr = ListToExpressionConverter.convert(schemeSymbol, context);
                return new MacroCallableExprNode(macroExpr, notEvaluatedArgs, context);
            }
        }

        var callable = ListToExpressionConverter.convert(operand, context);
        return CallableExprNodeGen.create(arguments, operand, callable);
    }

    private static List<SchemeExpression> getProcedureArguments(SchemeCell argumentList, ParsingContext context) {
        List<SchemeExpression> result = new ArrayList<>();

        for (Object obj : argumentList) {
            result.add(ListToExpressionConverter.convert(obj, context));
        }
        return result;
    }
}
