package com.ihorak.truffle.convertor.callable;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.ConverterContext;
import com.ihorak.truffle.exceptions.InterpreterException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.CallableExprNodeGen;
import com.ihorak.truffle.node.callable.TCO.throwers.TailCallThrowerNodeGen;
import com.ihorak.truffle.node.callable.TCO.throwers.TailRecursiveThrowerNodeGen;
import com.ihorak.truffle.node.scope.WriteFrameSlotNode;
import com.ihorak.truffle.node.scope.WriteFrameSlotNodeGen;
import com.ihorak.truffle.runtime.SchemeList;
import com.ihorak.truffle.runtime.SchemeSymbol;
import com.oracle.truffle.api.frame.FrameSlotKind;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ProcedureConverter {


    private static final int CTX_CALLABLE_INDEX = 1;

    private ProcedureConverter() {
    }

    public static SchemeExpression convert(SchemeList callableList, boolean isTailCallPosition, ConverterContext context, @Nullable ParserRuleContext procedureCtx) {
        var operandIR = callableList.car;
        List<SchemeExpression> arguments = CallableUtil.convertArguments(callableList.cdr, context, procedureCtx);
        var callableCtx = procedureCtx != null ? (ParserRuleContext) procedureCtx.getChild(CTX_CALLABLE_INDEX) : null;
        var operandExpr = InternalRepresentationConverter.convert(operandIR, context, false, false, callableCtx);

        if (isTailCallPosition) {
            if (isSelfTailRecursive(operandIR, context)) {
                return createSelfTailRecursiveThrower(arguments, context, procedureCtx);
            } else {
                return createTailCallThrower(arguments, operandExpr, operandIR, procedureCtx);
            }
        } else {
            // we need to allocate those since we detect TCO in runtime! 
            int tailCallArgumentsSlot = context.getFrameDescriptorBuilder().addSlot(FrameSlotKind.Object, null, null);
            int tailCallTargetSlot = context.getFrameDescriptorBuilder().addSlot(FrameSlotKind.Object, null, null);
            int tailCallResultSlot = context.getFrameDescriptorBuilder().addSlot(FrameSlotKind.Object, null, null);

            var callableExpr = CallableExprNodeGen.create(arguments, operandExpr, tailCallArgumentsSlot, tailCallTargetSlot, tailCallResultSlot);
            return SourceSectionUtil.setSourceSectionAndReturnExpr(callableExpr, procedureCtx);
        }

        //return SourceSectionUtil.setSourceSectionAndReturnExpr(new CallableExprNode(arguments, operandExpr), procedureCtx);
    }

    private static SchemeExpression createSelfTailRecursiveThrower(List<SchemeExpression> arguments, ConverterContext context, @Nullable ParserRuleContext procedureCtx) {
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


        var writeSlotNodes = createSelfTCOWriteFrameSlotsNodes(context);
        if (writeSlotNodes.size() != arguments.size()) InterpreterException.shouldNotReachHere();
        var selfTCOThrowerNode = TailRecursiveThrowerNodeGen.create(arguments, writeSlotNodes);
        return SourceSectionUtil.setSourceSectionAndReturnExpr(selfTCOThrowerNode, procedureCtx);
    }

    private static SchemeExpression createTailCallThrower(List<SchemeExpression> arguments, SchemeExpression operandExpr, Object operandIR, @Nullable ParserRuleContext procedureCtx) {
        var throwerNode = TailCallThrowerNodeGen.create(arguments, operandExpr, operandIR);
        return SourceSectionUtil.setSourceSectionAndReturnExpr(throwerNode, procedureCtx);

    }


    private static List<WriteFrameSlotNode> createSelfTCOWriteFrameSlotsNodes(ConverterContext context) {
        var frameSlotIndexes = context.getFunctionArgumentSlotIndexes().orElseThrow(InterpreterException::shouldNotReachHere);
        return frameSlotIndexes.stream().map(WriteFrameSlotNodeGen::create).toList();
    }


    private static boolean isSelfTailRecursive(Object operand, ConverterContext context) {
        if (context.getFunctionDefinitionName().isEmpty()) return false;
        if (context.isDefiningFunctionShadowed()) return false;

        return operand instanceof SchemeSymbol symbol && symbol.equals(context.getFunctionDefinitionName().get());
    }
}
