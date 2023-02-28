package com.ihorak.truffle.convertor.SpecialForms;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.context.LexicalScope;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.util.CreateWriteExprNode;
import com.ihorak.truffle.convertor.util.TailCallUtil;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNode;
import com.ihorak.truffle.node.special_form.LetExprNode;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;

public class LetConverter extends AbstractLetConverter {

    private LetConverter() {
    }

    public static LetExprNode convert(SchemeList letList, ParsingContext context, ParserRuleContext letCtx) {
        validate(letList);
        ParsingContext letContext = new ParsingContext(context, LexicalScope.LET, context.getFrameDescriptorBuilder());

        SchemeList localBindings = (SchemeList) letList.get(1);
        SchemeList body = letList.cdr().cdr();

        List<WriteLocalVariableExprNode> bindingExpressions = createWriteLocalVariables(localBindings, letContext, letCtx);
        List<SchemeExpression> bodyExpressions = TailCallUtil.convertBodyToSchemeExpressionsWithTCO(body, letContext, letCtx, CTX_BODY_INDEX);

        List<SchemeExpression> bindingsAndBodyExpressions = new ArrayList<>(bindingExpressions.size() + bodyExpressions.size());
        bindingsAndBodyExpressions.addAll(bindingExpressions);
        bindingsAndBodyExpressions.addAll(bodyExpressions);

        return new LetExprNode(bindingsAndBodyExpressions);

    }

    private static List<WriteLocalVariableExprNode> createWriteLocalVariables(SchemeList localBindings, ParsingContext context, ParserRuleContext letCtx) {
        List<WriteLocalVariableExprNode> result = new ArrayList<>();
        List<SchemeExpression> expressions = new ArrayList<>();
        List<SchemeSymbol> symbols = new ArrayList<>();
        var letParamCtx = (ParserRuleContext) letCtx.getChild(2).getChild(0);
        for (int i = 0; i < localBindings.size; i++) {
            var currentParamCtx =  letParamCtx.getChild(i + CTX_PARAMS_OFFSET).getChild(0);
            var exprCtx = (ParserRuleContext)currentParamCtx.getChild(2);
            var bindingList = (SchemeList) localBindings.get(i);
            var name = (SchemeSymbol) bindingList.get(0);
            var dataExpr = bindingList.get(1);
            var expr = InternalRepresentationConverter.convert(dataExpr, context, false, false, exprCtx);
            expressions.add(expr);
            symbols.add(name);
        }

        for (int i = 0; i < symbols.size(); i++) {
            var currentParamCtx =  letParamCtx.getChild(i + CTX_PARAMS_OFFSET).getChild(0);
            var identifierCtx = (ParserRuleContext)currentParamCtx.getChild(1);
            result.add(CreateWriteExprNode.createWriteLocalVariableExprNode(symbols.get(i), expressions.get(i), context, identifierCtx));
        }

        return result;
    }
}
