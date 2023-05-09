package com.ihorak.truffle.convertor.callable;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.context.ConverterContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.runtime.SchemeList;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CallableUtil {

    private CallableUtil() {

    }

    private static final int CTX_ARGUMENT_OFFSET = 2;


    public static List<SchemeExpression> convertArguments(SchemeList argumentListIR, ConverterContext context, @Nullable ParserRuleContext callableCtx) {
        List<SchemeExpression> result = new ArrayList<>();

        for (int i = 0; i < argumentListIR.size; i++) {
            var currentCtx = callableCtx != null ? (ParserRuleContext) callableCtx.getChild(i + CTX_ARGUMENT_OFFSET) : null;
            result.add(InternalRepresentationConverter.convert(argumentListIR.get(i), context, false, false, currentCtx));
        }
        return result;
    }
}
