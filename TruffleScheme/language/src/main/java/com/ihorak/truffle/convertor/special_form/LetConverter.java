package com.ihorak.truffle.convertor.special_form;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.util.CreateWriteExprNode;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNode;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LetConverter extends AbstractLetConverter {

    private LetConverter() {
    }

    public static SchemeExpression convert(SchemeList letList, ParsingContext context, boolean isTailCallPosition, @Nullable ParserRuleContext letCtx) {
        validate(letList);
        ParsingContext letContext = ParsingContext.createLetContext(context);

        var localBindingsIR = (SchemeList) letList.get(1);

        var writeLocalsExprs = createWriteLocalVariables(localBindingsIR, letContext, letCtx);
        return createLetExpr(letList, writeLocalsExprs, letContext, isTailCallPosition, context, letCtx);
    }

    private static List<WriteLocalVariableExprNode> createWriteLocalVariables(SchemeList localBindings, ParsingContext context, @Nullable ParserRuleContext letCtx) {
        List<WriteLocalVariableExprNode> result = new ArrayList<>();
        List<SchemeExpression> expressions = new ArrayList<>();
        List<SchemeSymbol> symbols = new ArrayList<>();
        var letParamCtx = letCtx != null ? (ParserRuleContext) letCtx.getChild(2).getChild(0) : null;
        for (int i = 0; i < localBindings.size; i++) {
            var exprCtx = getParameterExprCtx(letParamCtx, i);
            var bindingList = (SchemeList) localBindings.get(i);
            var name = (SchemeSymbol) bindingList.get(0);
            var exprIR = bindingList.get(1);
            var expr = InternalRepresentationConverter.convert(exprIR, context, false, false, exprCtx);
            expressions.add(expr);
            symbols.add(name);
        }

        for (int i = 0; i < symbols.size(); i++) {
            var identifierCtx = getIdentifierCtx(letParamCtx, i);
            result.add(CreateWriteExprNode.createWriteLocalVariableExprNode(symbols.get(i), expressions.get(i), context, identifierCtx));
        }

        return result;
    }
}
