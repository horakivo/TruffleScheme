package com.ihorak.truffle.convertor;

import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.util.BuiltinUtils;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.MacroCallableExprNode;
import com.ihorak.truffle.node.callable.TCO.SelfRecursiveTailCallThrowerNodeGen;
import com.ihorak.truffle.node.callable.TCO.TailCallCatcherNode;
import com.ihorak.truffle.node.callable.TCO.TailCallThrowerNodeGen;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.frame.FrameSlotKind;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;

public class CallableConverter {

    private static final int CTX_CALLABLE_INDEX = 1;
    private static final int CTX_ARGUMENT_OFFSET = 2;

    private CallableConverter() {
    }

    /*
     *  --> (operand argExpr1 ... argExprN)
     * procedureCtx is coming without form
     * */
    public static SchemeExpression convertListToProcedureCall(SchemeList callableList, ParsingContext context, boolean isTailCall, ParserRuleContext procedureCtx) {
        validate(callableList);

        if (isBuiltin(callableList)) {
            return createBuiltin(callableList, context, procedureCtx);
        } else if (isMacro(callableList, context)) {
            return createMacro(callableList, context);
        } else {
            //some callable procedure or potential runtime error
            return createProcedureCall(callableList, isTailCall, context, procedureCtx);
        }

    }

    private static boolean isSelfTailRecursive(Object operand, ParsingContext context) {
        var currentlyDefiningMethod = context.getFunctionDefinitionName();
        if (currentlyDefiningMethod == null) return false;

        return operand instanceof SchemeSymbol symbol && symbol.equals(currentlyDefiningMethod);
    }

    private static boolean isUnquote(SchemeSymbol schemeSymbol) {
        return schemeSymbol.getValue().equals("unquote");
    }

    private static boolean isUnquoteSplicing(SchemeSymbol schemeSymbol) {
        return schemeSymbol.getValue().equals("unquote-splicing");
    }

    private static boolean isBuiltin(SchemeList callableList) {
        return callableList.car() instanceof SchemeSymbol symbol && BuiltinUtils.isBuiltinProcedure(symbol);
    }

    private static boolean isMacro(SchemeList callableList, ParsingContext context) {
        return callableList.car() instanceof SchemeSymbol symbol && context.isMacro(symbol);

    }

    private static SchemeExpression createBuiltin(SchemeList callableList, ParsingContext context, ParserRuleContext procedureCtx) {
        var symbol = (SchemeSymbol) callableList.car();
        List<SchemeExpression> arguments = getProcedureArguments(callableList.cdr(), context, procedureCtx);
        return BuiltinConverter.createBuiltin(symbol, arguments, context, procedureCtx);
    }

    private static SchemeExpression createMacro(SchemeList callableList, ParsingContext context) {
        var symbol = (SchemeSymbol) callableList.car();
        List<Object> notEvaluatedArgs = new ArrayList<>();
        callableList.cdr().forEach(notEvaluatedArgs::add);
        var macroExpr = InternalRepresentationConverter.convert(symbol, context, false, false);
        return new MacroCallableExprNode(macroExpr, notEvaluatedArgs, context);
    }

    private static SchemeExpression createProcedureCall(SchemeList callableList, boolean isTailCall, ParsingContext context, ParserRuleContext procedureCtx) {
        var operand = callableList.car();
        List<SchemeExpression> arguments = getProcedureArguments(callableList.cdr(), context, procedureCtx);
        var callableCtx = (ParserRuleContext) procedureCtx.getChild(CTX_CALLABLE_INDEX);
        var callableExpr = InternalRepresentationConverter.convert(operand, context, false, false, callableCtx);
        //var callNode = new CallableExprNode(arguments, callable);
//

        if (isTailCall) {
            if (isSelfTailRecursive(operand, context)) {
                int tailRecursiveArgumentSlot = context.getFrameDescriptorBuilder().addSlot(FrameSlotKind.Object, null, null);
//                context.setSelfTailRecursionArgumentIndex(tailRecursiveArgumentSlot);
                return SelfRecursiveTailCallThrowerNodeGen.create(arguments);
            }
            return TailCallThrowerNodeGen.create(arguments, callableExpr);
        } else {
            int tailCallArgumentsSlot = context.getFrameDescriptorBuilder().addSlot(FrameSlotKind.Object, null, null);
            int tailCallTargetSlot = context.getFrameDescriptorBuilder().addSlot(FrameSlotKind.Object, null, null);
            var catcherNode = new TailCallCatcherNode(arguments, callableExpr, tailCallArgumentsSlot, tailCallTargetSlot);
            return SourceSectionUtil.setSourceSectionAndReturnExpr(catcherNode, procedureCtx);
            //return  new CallableExprNode(arguments, callable);
        }

        // return callNode;
    }

    private static List<SchemeExpression> getProcedureArguments(SchemeList argumentList, ParsingContext context, ParserRuleContext procedureCtx) {
        List<SchemeExpression> result = new ArrayList<>();

        for (int i = 0; i < argumentList.size; i++) {
            var currentCtx = (ParserRuleContext) procedureCtx.getChild(i + CTX_ARGUMENT_OFFSET);
            result.add(InternalRepresentationConverter.convert(argumentList.get(i), context, false, false, currentCtx));
        }
        return result;
    }

    private static void validate(SchemeList callableList) {
        var operand = callableList.car();
        if (operand instanceof SchemeSymbol schemeSymbol) {
            if (isUnquote(schemeSymbol))
                throw new SchemeException("unquote: expression not valid outside of quasiquote in form " + callableList, null);
            if (isUnquoteSplicing(schemeSymbol))
                throw new SchemeException("unquote-splicing: expression not valid outside of quasiquote in form " + callableList, null);

        }
    }

}
