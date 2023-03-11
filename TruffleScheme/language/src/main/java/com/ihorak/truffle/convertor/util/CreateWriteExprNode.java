package com.ihorak.truffle.convertor.util;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.scope.WriteGlobalVariableExprNode;
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNode;
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNodeGen;
import com.ihorak.truffle.type.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;

public class CreateWriteExprNode {

    private CreateWriteExprNode() {}

    /**
     * ParserRuleContext symbolCtx - ContextForm can be send here since the start and stop index are same
     * */

    public static WriteLocalVariableExprNode createWriteLocalVariableExprNode(SchemeSymbol name, SchemeExpression valueToWrite, ParsingContext context, ParserRuleContext symbolCtx) {
        var index = context.findOrAddLocalSymbol(name);
        var expr =  WriteLocalVariableExprNodeGen.create(index, valueToWrite);
        SourceSectionUtil.setSourceSection(expr, symbolCtx);

        return expr;
    }

    public static SchemeExpression createWriteGlobalVariableExprNode(SchemeSymbol name, SchemeExpression valueToStore, ParserRuleContext symbolCtx) {
        var expr = new WriteGlobalVariableExprNode(valueToStore, name);
        SourceSectionUtil.setSourceSection(expr, symbolCtx);

        return expr;
    }

}
