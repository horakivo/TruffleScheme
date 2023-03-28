package com.ihorak.truffle.convertor.callable;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.MacroCallableExprNode;
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
        var argumentListIR = callableList.cdr;
        var macroTransformationInfoInfo = context.getMacroTransformationInfo(symbolIR);

        if (argumentListIR.size != macroTransformationInfoInfo.amountOfArgs()) {
            throw new SchemeException("""
                    macro %s was called with wrong number of arguments
                    expected: %d
                    given: %d
                    """.formatted(symbolIR, macroTransformationInfoInfo.amountOfArgs(), argumentListIR.size), null);
        }

        List<Object> notEvaluatedArgs = new ArrayList<>();
        argumentListIR.forEach(notEvaluatedArgs::add);

        var macroExpr = new MacroCallableExprNode(macroTransformationInfoInfo.callTarget(), notEvaluatedArgs, context);
        return SourceSectionUtil.setSourceSectionAndReturnExpr(macroExpr, macroCtx);
    }
}



