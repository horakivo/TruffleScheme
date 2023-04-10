package com.ihorak.truffle.convertor.util;

import com.ihorak.truffle.convertor.ConverterException;
import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.InterpreterException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.scope.StoreSelfTailCallResultInFrame;
import com.ihorak.truffle.node.scope.StoreSelfTailCallResultInFrameNodeGen;
import com.ihorak.truffle.type.SchemeList;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TailCallUtil {

    private TailCallUtil() {
    }


    //body is e.g. body of lambda or let where definitions+ are allowed
    //TCO: (<definitions>+ <expressions>* <tail expression>)

    /**
     * ctx - Parser context (e.g lambda, or, and, let)
     * ctxBodyStartIndex - since the body is defined as follows: <definitions>+ <expressions>* we don't know where in the
     * lambda, or we should start. This index is the starting point
     */
    public static List<SchemeExpression> convertWithDefinitionsAndWithFrameCreation(SchemeList bodyIR, ParsingContext context, @Nullable ParserRuleContext ctx, int ctxBodyStartIndex) {
        List<SchemeExpression> result = new ArrayList<>();
        var size = bodyIR.size;
        for (int i = 0; i < size - 1; i++) {
            var currentCtx = getCurrentBodyCtx(ctx, ctxBodyStartIndex, i);
            result.add(InternalRepresentationConverter.convert(bodyIR.get(i), context, false, true, currentCtx));
        }

        //TCO: (or <expression>* <tail expression>)
        if (size > 0) {
            var lastIndex = size - 1;
            var currentCtx = getCurrentBodyCtx(ctx, ctxBodyStartIndex, lastIndex);

            var lastExpr = InternalRepresentationConverter.convert(bodyIR.get(size - 1), context, true, false, currentCtx);
            if (context.isFunctionSelfTailRecursive()) {
                var resultFrameIndex = context.getSelfTCOResultFrameSlot().orElseThrow(ConverterException::shouldNotReachHere);
                lastExpr = StoreSelfTailCallResultInFrameNodeGen.create(lastExpr, resultFrameIndex);
            }
            result.add(lastExpr);
        }

        return result;
    }

    public static List<SchemeExpression> convertWithDefinitionsAndNoFrameCreation(SchemeList expressionsIR, ParsingContext context, boolean isTailCallPosition, @Nullable ParserRuleContext ctx, int ctxBodyStartIndex) {
        return convertWithNoFrameCreation(expressionsIR, context, isTailCallPosition, ctx, ctxBodyStartIndex, true);
    }


    //TCO: (or <expression>* <tail expression>)
    public static List<SchemeExpression> convertWithNoDefinitionsAndNoFrameCreation(SchemeList expressionsIR, ParsingContext context, boolean isTailCallPosition, @Nullable ParserRuleContext ctx, int ctxBodyStartIndex) {
        return convertWithNoFrameCreation(expressionsIR, context, isTailCallPosition, ctx, ctxBodyStartIndex, false);
    }


    private static List<SchemeExpression> convertWithNoFrameCreation(SchemeList expressionsIR, ParsingContext context, boolean isTailCallPosition, @Nullable ParserRuleContext ctx, int ctxBodyStartIndex, boolean definitionAllowed) {
        List<SchemeExpression> result = new ArrayList<>();
        var size = expressionsIR.size;
        for (int i = 0; i < size - 1; i++) {
            var currentCtx = getCurrentBodyCtx(ctx, ctxBodyStartIndex, i);
            result.add(InternalRepresentationConverter.convert(expressionsIR.get(i), context, false, definitionAllowed, currentCtx));
        }

        if (size > 0) {
            var lastIndex = size - 1;
            var currentCtx = getCurrentBodyCtx(ctx, ctxBodyStartIndex, lastIndex);
            result.add(InternalRepresentationConverter.convert(expressionsIR.get(lastIndex), context, isTailCallPosition, false, currentCtx));
        }

        return result;
    }


    private static ParserRuleContext getCurrentBodyCtx(@Nullable ParserRuleContext ctx, int ctxBodyStartIndex, int index) {
        if (ctx == null) return null;

        return (ParserRuleContext) ctx.getChild(ctxBodyStartIndex + index);
    }

//    private static SchemeExpression handlePotentialSelfTailRecursion(SchemeExpression lastExpr) {
//        if (lastExpr instanceof IfElseExprNode) {
//
//        }
//    }
//
//    private static boolean isIfElseExprSelfTCO(IfElseExprNode ifElseExprNode) {
//
//    }
}
