package com.ihorak.truffle.convertor.callable;

import com.ihorak.truffle.convertor.ConverterException;
import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.polyglot.InvokeOrReadExprNodeGen;
import com.ihorak.truffle.runtime.SchemeList;
import com.ihorak.truffle.runtime.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

public class PolyglotConverter {

    private PolyglotConverter() {

    }

    public static SchemeExpression convert(SchemeList callableList, ParsingContext context, @Nullable ParserRuleContext callableCtx) {
        validate(callableList);
        var identifier = (SchemeSymbol) callableList.get(1);
        var receiverExpr = getReceiverExpr(callableList, context, callableCtx);
        var argumentsListIR = callableList.cdr.cdr.cdr;
        var arguments = CallableUtil.convertArguments(argumentsListIR, context, callableCtx);

        var invokeOrRead = InvokeOrReadExprNodeGen.create(receiverExpr, arguments, identifier);
        return SourceSectionUtil.setSourceSectionAndReturnExpr(invokeOrRead, callableCtx);
    }

    private static SchemeExpression getReceiverExpr(SchemeList callableList, ParsingContext context, @Nullable ParserRuleContext callableCtx) {
        var receiverIR = callableList.get(2);
        var receiverCtx = callableCtx != null ? (ParserRuleContext) callableCtx.getChild(3) : null;
        return InternalRepresentationConverter.convert(receiverIR, context, false, false, receiverCtx);
    }

    private static void validate(SchemeList callableList) {
        if (callableList.size < 2) {
            throw ConverterException.arityExceptionAtLeast(".", 2, callableList.size);
        }

        var symbol = callableList.get(1);
        if (!(symbol instanceof SchemeSymbol)) {
            throw ConverterException.contractViolation(".", "symbol?", symbol);
        }
    }
}
