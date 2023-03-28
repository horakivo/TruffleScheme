package com.ihorak.truffle.convertor.SpecialForms;

import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.util.TailCallUtil;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeList;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

public abstract class AndOrAbstractConverter {


    private static final int CTX_BODY_INDEX = 2;


    protected static List<SchemeExpression> getBodyExpr(SchemeList bodyListIR, boolean isTailCallPosition, ParsingContext context, ParserRuleContext ctx) {
        return TailCallUtil.convertWithNoDefinitionsAndNoFrameCreation(bodyListIR, context, isTailCallPosition, ctx, CTX_BODY_INDEX);
    }
}
