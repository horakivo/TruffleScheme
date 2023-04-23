package com.ihorak.truffle.convertor.callable;

import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.runtime.SchemeList;
import com.ihorak.truffle.runtime.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

public class CallableConverter {

    private CallableConverter() {
    }

    /*
     *  --> (operand argExpr1 ... argExprN)
     * */
    public static SchemeExpression convertListToProcedureCall(SchemeList callableList, ParsingContext context, boolean isTailCall, @Nullable ParserRuleContext callableCtx) {
        validate(callableList);

        if (isPolyglot(callableList)) {
            return PolyglotConverter.convert(callableList, context, callableCtx);
        } else if (isMacro(callableList, context)) {
            return MacroConverter.convert(callableList, context, callableCtx);
        } else {
            return ProcedureConverter.convert(callableList, isTailCall, context, callableCtx);
        }

    }

    private static boolean isPolyglot(SchemeList callableList) {
        return callableList.car instanceof SchemeSymbol symbol && symbol.value().equals(".");
    }


    private static boolean isMacro(SchemeList callableList, ParsingContext context) {
        return callableList.car instanceof SchemeSymbol symbol && context.isMacro(symbol);

    }

    private static void validate(SchemeList callableList) {
        var operand = callableList.car;
        if (operand instanceof SchemeSymbol schemeSymbol) {
            if (isUnquote(schemeSymbol))
                throw new SchemeException("unquote: expression not valid outside of quasiquote in form " + callableList, null);
            if (isUnquoteSplicing(schemeSymbol))
                throw new SchemeException("unquote-splicing: expression not valid outside of quasiquote in form " + callableList, null);

        }
    }

    private static boolean isUnquote(SchemeSymbol schemeSymbol) {
        return schemeSymbol.value().equals("unquote");
    }

    private static boolean isUnquoteSplicing(SchemeSymbol schemeSymbol) {
        return schemeSymbol.value().equals("unquote-splicing");
    }

}
