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
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNode;
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNodeGen;
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
        var operandIR = callableList.car();
        List<SchemeExpression> arguments = getProcedureArguments(callableList.cdr(), context, procedureCtx);
        var callableCtx = (ParserRuleContext) procedureCtx.getChild(CTX_CALLABLE_INDEX);
        var operandExpr = InternalRepresentationConverter.convert(operandIR, context, false, false, callableCtx);

        if (isTailCall) {
            if (isSelfTailRecursive(operandIR, context)) {
                int numberOfArguments = context.getFunctionNumberOfArguments().orElseThrow(InterpreterException::shouldNotReachHere);


                /*
                 * This is needed because there can be multiple Self Tail calls
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
                List<Integer> argumentsSlotIndexes = context.getSelfTailRecursionArgumentsSlotIndexes()
                        .orElseGet(() -> {
                            var argsSlotIndexes = getArgumentsSlotIndexes(numberOfArguments, context);
                            var resultSlotIndex = context.getFrameDescriptorBuilder().addSlot(FrameSlotKind.Object, null, null);
                            context.setSelfTailRecursionArgumentIndexes(argsSlotIndexes);
                            context.setSelfTailRecursionResultIndex(resultSlotIndex);
                            return argsSlotIndexes;
                        });

                if (argumentsSlotIndexes.size() != numberOfArguments) InterpreterException.shouldNotReachHere("Horribly wrong! Converter bug during Self TCO analysis");
                return SelfRecursiveTailCallThrowerNodeGen.create(createWriteArgumentsExprs(argumentsSlotIndexes, arguments));
            }
            context.setDefiningProcedureAsTailCall();
            var throwerNode = TailCallThrowerNodeGen.create(arguments, operandExpr, operandIR);
            return SourceSectionUtil.setSourceSectionAndReturnExpr(throwerNode, procedureCtx);
        } else {
            if (isCallableTailCallProcedure(operandIR, context)) {
                int tailCallArgumentsSlot = context.getFrameDescriptorBuilder().addSlot(FrameSlotKind.Object, null, null);
                int tailCallTargetSlot = context.getFrameDescriptorBuilder().addSlot(FrameSlotKind.Object, null, null);
                var tailCallCatcherNode = new TailCallCatcherNode(arguments, operandExpr, tailCallArgumentsSlot, tailCallTargetSlot);
                return SourceSectionUtil.setSourceSectionAndReturnExpr(tailCallCatcherNode, procedureCtx);
            }

            var callableExpr = new CallableExprNode(arguments, operandExpr);
            return SourceSectionUtil.setSourceSectionAndReturnExpr(callableExpr, procedureCtx);
        }

        //return SourceSectionUtil.setSourceSectionAndReturnExpr(new CallableExprNode(arguments, operandExpr), procedureCtx);
    }


    private static List<Integer> getArgumentsSlotIndexes(int numberOfArguments, ParsingContext context) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < numberOfArguments; i++) {
            result.add(context.getFrameDescriptorBuilder().addSlot(FrameSlotKind.Illegal, null, null));
        }

        return result;
    }

    private static List<WriteLocalVariableExprNode> createWriteArgumentsExprs(List<Integer> argumentsSlotIndexes, List<SchemeExpression> arguments) {
        if (arguments.size() != argumentsSlotIndexes.size()) throw InterpreterException.shouldNotReachHere();

        List<WriteLocalVariableExprNode> result = new ArrayList<>();
        for (int i = 0; i < argumentsSlotIndexes.size(); i++) {
            var index = argumentsSlotIndexes.get(i);
            var expr = arguments.get(i);
            result.add(WriteLocalVariableExprNodeGen.create(index, expr));
        }

        return result;
    }


    private static boolean isCallableTailCallProcedure(Object operand, ParsingContext context) {
        if (operand instanceof SchemeSymbol symbol) {
            return context.isProcedureTailCall(symbol);
        }

        return false;
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
