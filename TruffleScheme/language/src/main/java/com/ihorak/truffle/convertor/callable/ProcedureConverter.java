package com.ihorak.truffle.convertor.callable;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.InterpreterException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.CallableExprNode;
import com.ihorak.truffle.node.callable.TCO.SelfRecursiveTailCallThrowerNodeGen;
import com.ihorak.truffle.node.callable.TCO.TailCallThrowerNodeGen;
import com.ihorak.truffle.node.scope.WriteFrameSlotNode;
import com.ihorak.truffle.node.scope.WriteFrameSlotNodeGen;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.frame.FrameSlotKind;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ProcedureConverter extends AbstractCallableConverter {


    private static final int CTX_CALLABLE_INDEX = 1;

    private ProcedureConverter() {
    }

    public static SchemeExpression convert(SchemeList callableList, boolean isTailCall, ParsingContext context, @Nullable ParserRuleContext procedureCtx) {
        var operandIR = callableList.car;
        List<SchemeExpression> arguments = convertArguments(callableList.cdr, context, procedureCtx);
        var callableCtx = procedureCtx != null ? (ParserRuleContext) procedureCtx.getChild(CTX_CALLABLE_INDEX) : null;
        var operandExpr = InternalRepresentationConverter.convert(operandIR, context, false, false, callableCtx);

        if (isTailCall) {
            if (isSelfTailRecursive(operandIR, context)) {
                return createSelfTailRecursiveThrower(arguments, context, procedureCtx);
            }
            return createTailCallThrower(arguments, operandExpr, operandIR, procedureCtx);
        } else {
            // we need to allocate those since we detect TCO in runtime! 
            int tailCallArgumentsSlot = context.getFrameDescriptorBuilder().addSlot(FrameSlotKind.Object, null, null);
            int tailCallTargetSlot = context.getFrameDescriptorBuilder().addSlot(FrameSlotKind.Object, null, null);
            int tailCallResultSlot = context.getFrameDescriptorBuilder().addSlot(FrameSlotKind.Object, null, null);

            var callableExpr = new CallableExprNode(arguments, operandExpr, tailCallArgumentsSlot, tailCallTargetSlot, tailCallResultSlot);
            return SourceSectionUtil.setSourceSectionAndReturnExpr(callableExpr, procedureCtx);
        }

        //return SourceSectionUtil.setSourceSectionAndReturnExpr(new CallableExprNode(arguments, operandExpr), procedureCtx);
    }

    private static SchemeExpression createSelfTailRecursiveThrower(List<SchemeExpression> arguments, ParsingContext context, @Nullable ParserRuleContext procedureCtx) {
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
        var selfTCOThrowerNode = SelfRecursiveTailCallThrowerNodeGen.create(arguments, writeSlotNodes);
        return SourceSectionUtil.setSourceSectionAndReturnExpr(selfTCOThrowerNode, procedureCtx);
    }

    private static SchemeExpression createTailCallThrower(List<SchemeExpression> arguments, SchemeExpression operandExpr, Object operandIR, @Nullable ParserRuleContext procedureCtx) {
        var throwerNode = TailCallThrowerNodeGen.create(arguments, operandExpr, operandIR);
        return SourceSectionUtil.setSourceSectionAndReturnExpr(throwerNode, procedureCtx);
    }


    private static List<WriteFrameSlotNode> createSelfTCOWriteFrameSlotsNodes(ParsingContext context) {
        var frameSlotIndexes = context.getFunctionArgumentSlotIndexes().orElseThrow(InterpreterException::shouldNotReachHere);
        return frameSlotIndexes.stream().map(WriteFrameSlotNodeGen::create).toList();
    }


    private static boolean isSelfTailRecursive(Object operand, ParsingContext context) {
        if (context.getFunctionDefinitionName().isEmpty()) return false;
        return operand instanceof SchemeSymbol symbol && symbol.equals(context.getFunctionDefinitionName().get());
    }
}
