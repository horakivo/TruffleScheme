package com.ihorak.truffle.convertor.SpecialForms;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.SourceSectionUtil;
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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LetConverter extends AbstractLetConverter {

    private LetConverter() {
    }

    public static SchemeExpression convert(SchemeList letList, ParsingContext context, @Nullable ParserRuleContext letCtx) {
        validate(letList);
        ParsingContext letContext = new ParsingContext(context, LexicalScope.LET, context.getFrameDescriptorBuilder(), context.getSource());

        SchemeList localBindings = (SchemeList) letList.get(1);
        SchemeList body = letList.cdr().cdr();

        List<WriteLocalVariableExprNode> bindingExpressions = createWriteLocalVariables(localBindings, letContext, letCtx);
        List<SchemeExpression> bodyExpressions = TailCallUtil.convertBodyToSchemeExpressionsWithTCO(body, letContext, letCtx, CTX_BODY_INDEX);

        List<SchemeExpression> bindingsAndBodyExpressions = new ArrayList<>(bindingExpressions.size() + bodyExpressions.size());
        bindingsAndBodyExpressions.addAll(bindingExpressions);
        bindingsAndBodyExpressions.addAll(bodyExpressions);

        return SourceSectionUtil.setSourceSectionAndReturnExpr(new LetExprNode(bindingsAndBodyExpressions), letCtx);

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
            var dataExpr = bindingList.get(1);
            var expr = InternalRepresentationConverter.convert(dataExpr, context, false, false, exprCtx);
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
