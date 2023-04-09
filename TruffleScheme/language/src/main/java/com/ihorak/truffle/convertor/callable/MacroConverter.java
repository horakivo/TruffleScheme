package com.ihorak.truffle.convertor.callable;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.MacroCallableExprNode;
import com.ihorak.truffle.node.callable.MacroCallableExprNodeGen;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MacroConverter {

    private MacroConverter() {
    }

    public static SchemeExpression convert(SchemeList callableList, ParsingContext context, @Nullable ParserRuleContext macroCtx) {
        var symbolIR = (SchemeSymbol) callableList.car;
        var transformationExpr = context.getMacroTransformationExpr(symbolIR);
        List<Object> notEvaluatedArgs = getMacroArguments(callableList);

        var macroExpr = MacroCallableExprNodeGen.create(transformationExpr, notEvaluatedArgs, context, symbolIR);
        return SourceSectionUtil.setSourceSectionAndReturnExpr(macroExpr, macroCtx);
    }

    // Returns not evaluated arguments
    private static List<Object> getMacroArguments(SchemeList callableList) {
        var argumentListIR = callableList.cdr;

        List<Object> notEvaluatedArgs = new ArrayList<>();
        argumentListIR.forEach(notEvaluatedArgs::add);

        return notEvaluatedArgs;
    }
}



