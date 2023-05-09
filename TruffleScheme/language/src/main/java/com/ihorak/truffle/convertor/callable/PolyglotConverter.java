package com.ihorak.truffle.convertor.callable;

import com.ihorak.truffle.convertor.ConverterException;
import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.ConverterContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.polyglot.InvokeOrReadExprNodeGen;
import com.ihorak.truffle.node.polyglot.SetValueExprNodeGen;
import com.ihorak.truffle.runtime.SchemeList;
import com.ihorak.truffle.runtime.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

public class PolyglotConverter {

    private PolyglotConverter() {

    }

    public static SchemeExpression convert(SchemeList callableList, ConverterContext context, @Nullable ParserRuleContext callableCtx) {
        validate(callableList);
        var operation = (SchemeSymbol) callableList.car;

        return switch (operation.value()) {
            case "." -> createInvokeOrReadExpr(callableList, context, callableCtx);
            case "set-value!" -> createSetValueExpr(callableList, context, callableCtx);
            default -> throw ConverterException.shouldNotReachHere();
        };
    }

    private static SchemeExpression createSetValueExpr(SchemeList callableList, ConverterContext context, @Nullable ParserRuleContext callableCtx) {
        if (callableList.size != 4) {
            throw ConverterException.arityException("set-value!", 3, callableList.size - 1);
        }
        var identifier = (SchemeSymbol) callableList.get(1);
        var receiverExpr = getReceiverExpr(callableList, context, callableCtx);
        var valueToStoreIR = callableList.get(3);
        var valueToStoreCtx = callableCtx != null ? (ParserRuleContext) callableCtx.getChild(4) : null;
        var valueToStoreExpr = InternalRepresentationConverter.convert(valueToStoreIR, context, false, false, valueToStoreCtx);

        var setValueExpr = SetValueExprNodeGen.create(receiverExpr, valueToStoreExpr, identifier);
        return SourceSectionUtil.setSourceSectionAndReturnExpr(setValueExpr, callableCtx);
    }

    private static SchemeExpression createInvokeOrReadExpr(SchemeList callableList, ConverterContext context, @Nullable ParserRuleContext callableCtx) {
        if (callableList.size < 2) {
            throw ConverterException.arityExceptionAtLeast(".", 2, callableList.size);
        }
        var identifier = (SchemeSymbol) callableList.get(1);
        var receiverExpr = getReceiverExpr(callableList, context, callableCtx);
        var argumentsListIR = callableList.cdr.cdr.cdr;
        var arguments = CallableUtil.convertArguments(argumentsListIR, context, callableCtx);

        var invokeOrRead = InvokeOrReadExprNodeGen.create(receiverExpr, arguments, identifier);
        return SourceSectionUtil.setSourceSectionAndReturnExpr(invokeOrRead, callableCtx);
    }

    private static SchemeExpression getReceiverExpr(SchemeList callableList, ConverterContext context, @Nullable ParserRuleContext callableCtx) {
        var receiverIR = callableList.get(2);
        var receiverCtx = callableCtx != null ? (ParserRuleContext) callableCtx.getChild(3) : null;
        return InternalRepresentationConverter.convert(receiverIR, context, false, false, receiverCtx);
    }

    private static void validate(SchemeList callableList) {
        var identifier = callableList.get(1);
        if (!(identifier instanceof SchemeSymbol)) {
            var operation = (SchemeSymbol) callableList.car;
            throw ConverterException.contractViolation(operation.value(), "symbol?", identifier);
        }
    }

    public static boolean isPolyglot(SchemeSymbol symbol) {
        return switch (symbol.value()) {
            case ".", "set-value!" -> true;
            default -> false;
        };
    }
}
