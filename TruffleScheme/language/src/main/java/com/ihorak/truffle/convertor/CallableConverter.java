package com.ihorak.truffle.convertor;

import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.util.BuiltinUtils;
import com.ihorak.truffle.exceptions.InterpreterException;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.CallableExprNode;
import com.ihorak.truffle.node.callable.MacroCallableExprNode;
import com.ihorak.truffle.node.callable.TCO.SelfRecursiveTailCallThrowerNodeGen;
import com.ihorak.truffle.node.callable.TCO.TailCallCatcherNode;
import com.ihorak.truffle.node.callable.TCO.TailCallThrowerNodeGen;
import com.ihorak.truffle.node.scope.ReadLocalVariableExprNodeGen;
import com.ihorak.truffle.node.scope.WriteFrameSlotNode;
import com.ihorak.truffle.node.scope.WriteFrameSlotNodeGen;
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNode;
import com.ihorak.truffle.node.special_form.LambdaExprNode;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.frame.FrameSlotKind;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

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
    public static SchemeExpression convertListToProcedureCall(SchemeList callableList, ParsingContext context, boolean isTailCall, @Nullable ParserRuleContext callableCtx) {
        validate(callableList);

        if (isBuiltin(callableList)) {
            return createBuiltin(callableList, context, callableCtx);
        } else if (isMacro(callableList, context)) {
            return createMacro(callableList, context, callableCtx);
        } else {
            //some callable procedure or potential runtime error
            return createProcedureCall(callableList, isTailCall, context, callableCtx);
        }

    }

    private static boolean isSelfTailRecursive(Object operand, ParsingContext context) {
        if (context.getFunctionDefinitionName().isEmpty()) return false;
        return operand instanceof SchemeSymbol symbol && symbol.equals(context.getFunctionDefinitionName().get());
    }

    private static boolean isUnquote(SchemeSymbol schemeSymbol) {
        return schemeSymbol.getValue().equals("unquote");
    }

    private static boolean isUnquoteSplicing(SchemeSymbol schemeSymbol) {
        return schemeSymbol.getValue().equals("unquote-splicing");
    }

    private static boolean isBuiltin(SchemeList callableList) {
        return callableList.car instanceof SchemeSymbol symbol && BuiltinUtils.isBuiltinProcedure(symbol);
    }

    private static boolean isMacro(SchemeList callableList, ParsingContext context) {
        return callableList.car instanceof SchemeSymbol symbol && context.isMacro(symbol);

    }

    private static SchemeExpression createBuiltin(SchemeList callableList, ParsingContext context, @Nullable ParserRuleContext procedureCtx) {
        var symbol = (SchemeSymbol) callableList.car;
        List<SchemeExpression> arguments = getProcedureArguments(callableList.cdr, context, procedureCtx);
        return BuiltinConverter.createBuiltin(symbol, arguments, context, procedureCtx);
    }

    private static SchemeExpression createMacro(SchemeList callableList, ParsingContext context, @Nullable ParserRuleContext macroCtx) {
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

    private static SchemeExpression createProcedureCall(SchemeList callableList, boolean isTailCall, ParsingContext context, @Nullable ParserRuleContext procedureCtx) {
        var operandIR = callableList.car;
        List<SchemeExpression> arguments = getProcedureArguments(callableList.cdr, context, procedureCtx);
        var callableCtx = procedureCtx != null ? (ParserRuleContext) procedureCtx.getChild(CTX_CALLABLE_INDEX) : null;
        var operandExpr = InternalRepresentationConverter.convert(operandIR, context, false, false, callableCtx);

        if (isTailCall) {
            if (isSelfTailRecursive(operandIR, context)) {
                /*
                 * This setFunctionAsSelfTailRecursive can be called twice in the example below
                 * e.g.
                 * (define partition
                 *   (lambda (piv l p1 p2)
                 *     (if (null? l)
                 *         (list p1 p2)
                 *         (if (< (car l) piv)
                 *             (partition piv (cdr l) (cons (car l) p1) p2)
                 *             (partition piv (cdr l) p1 (cons (car l) p2))))))
                 *
                 * */
                context.setFunctionAsSelfTailRecursive();
                if (context.getSelfTCOResultFrameSlot().isEmpty()) {
                    var resultIndex = context.getFrameDescriptorBuilder().addSlot(FrameSlotKind.Object, null, null);
                    context.setSelfTailRecursionResultIndex(resultIndex);
                }


                var writeSlotNodes = createWriteFrameSlotsNodes(context);
                if (writeSlotNodes.size() != arguments.size()) InterpreterException.shouldNotReachHere();
                return SelfRecursiveTailCallThrowerNodeGen.create(arguments, writeSlotNodes);
            }
            context.setDefiningProcedureAsTailCall();
            var throwerNode = TailCallThrowerNodeGen.create(arguments, operandExpr, operandIR);
            return SourceSectionUtil.setSourceSectionAndReturnExpr(throwerNode, procedureCtx);
        } else {
            int tailCallArgumentsSlot = context.getFrameDescriptorBuilder().addSlot(FrameSlotKind.Object, null, null);
            int tailCallTargetSlot = context.getFrameDescriptorBuilder().addSlot(FrameSlotKind.Object, null, null);
            int tailCallResultSlot = context.getFrameDescriptorBuilder().addSlot(FrameSlotKind.Object, null, null);

            var callableExpr = new CallableExprNode(arguments, operandExpr, tailCallArgumentsSlot, tailCallTargetSlot, tailCallResultSlot);
            return SourceSectionUtil.setSourceSectionAndReturnExpr(callableExpr, procedureCtx);
        }

        //return SourceSectionUtil.setSourceSectionAndReturnExpr(new CallableExprNode(arguments, operandExpr), procedureCtx);
    }

    private static List<WriteFrameSlotNode> createWriteFrameSlotsNodes(ParsingContext context) {
        var frameSlotIndexes = context.getFunctionArgumentSlotIndexes();
        return frameSlotIndexes.stream().map(WriteFrameSlotNodeGen::create).toList();
    }

    private static boolean isCallableTailCallProcedure(Object operand, SchemeExpression operandExpr, ParsingContext context) {
        if (operand instanceof SchemeSymbol symbol) {
            if (context.getFunctionDefinitionName().isPresent() && symbol.equals(context.getFunctionDefinitionName().get())) {
                // TODO is this valid assumption?
                // calling self in non-tail call position - does it have to be catcher?
                return false;
            }
            return context.isProcedureTailCall(symbol);
        }

        if (operandExpr instanceof LambdaExprNode lambdaExpr) {
            return lambdaExpr.isTailCall;
        }

        // we have to assume that it is a catcher
        return true;
    }

    private static List<SchemeExpression> getProcedureArguments(SchemeList argumentList, ParsingContext context, @Nullable ParserRuleContext procedureCtx) {
        List<SchemeExpression> result = new ArrayList<>();

        for (int i = 0; i < argumentList.size; i++) {
            var currentCtx = procedureCtx != null ? (ParserRuleContext) procedureCtx.getChild(i + CTX_ARGUMENT_OFFSET) : null;
            result.add(InternalRepresentationConverter.convert(argumentList.get(i), context, false, false, currentCtx));
        }
        return result;
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

}
