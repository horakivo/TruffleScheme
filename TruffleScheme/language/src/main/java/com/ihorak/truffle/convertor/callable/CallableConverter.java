package com.ihorak.truffle.convertor.callable;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.util.BuiltinUtils;
import com.ihorak.truffle.exceptions.InterpreterException;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.CallableExprNode;
import com.ihorak.truffle.node.callable.MacroCallableExprNode;
import com.ihorak.truffle.node.callable.TCO.SelfRecursiveTailCallThrowerNodeGen;
import com.ihorak.truffle.node.callable.TCO.TailCallThrowerNodeGen;
import com.ihorak.truffle.node.scope.WriteFrameSlotNode;
import com.ihorak.truffle.node.scope.WriteFrameSlotNodeGen;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.frame.FrameSlotKind;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CallableConverter {

    private CallableConverter() {
    }

    /*
     *  --> (operand argExpr1 ... argExprN)
     * */
    public static SchemeExpression convertListToProcedureCall(SchemeList callableList, ParsingContext context, boolean isTailCall, @Nullable ParserRuleContext callableCtx) {
        validate(callableList);

        if (isBuiltin(callableList)) {
            return BuiltinConverter.convert(callableList, context, callableCtx);
        } else if (isMacro(callableList, context)) {
            return MacroConverter.convert(callableList, context, callableCtx);
        } else {
            return ProcedureConverter.convert(callableList, isTailCall, context, callableCtx);
        }

    }

    private static boolean isBuiltin(SchemeList callableList) {
        return callableList.car instanceof SchemeSymbol symbol && BuiltinUtils.isBuiltinProcedure(symbol);
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
        return schemeSymbol.getValue().equals("unquote");
    }

    private static boolean isUnquoteSplicing(SchemeSymbol schemeSymbol) {
        return schemeSymbol.getValue().equals("unquote-splicing");
    }

}
