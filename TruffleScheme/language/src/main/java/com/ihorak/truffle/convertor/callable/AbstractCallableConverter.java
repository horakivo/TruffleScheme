package com.ihorak.truffle.convertor.callable;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.runtime.SchemeList;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCallableConverter {

    private static final int CTX_ARGUMENT_OFFSET = 2;


    protected static List<SchemeExpression> convertArguments(SchemeList argumentListIR, ParsingContext context, @Nullable ParserRuleContext procedureCtx) {
        List<SchemeExpression> result = new ArrayList<>();

        for (int i = 0; i < argumentListIR.size; i++) {
            var currentCtx = procedureCtx != null ? (ParserRuleContext) procedureCtx.getChild(i + CTX_ARGUMENT_OFFSET) : null;
            result.add(InternalRepresentationConverter.convert(argumentListIR.get(i), context, false, false, currentCtx));
        }
        return result;
    }
}
