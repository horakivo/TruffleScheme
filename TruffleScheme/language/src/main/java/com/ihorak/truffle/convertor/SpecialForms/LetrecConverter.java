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
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LetrecConverter extends AbstractLetConverter {

    private LetrecConverter() {
    }

    //TODO solve code duplication
    public static SchemeExpression convert(SchemeList letList, ParsingContext context, @Nullable ParserRuleContext letrecCtx) {
        validate(letList);
        ParsingContext letContext = new ParsingContext(context, LexicalScope.LETREC, context.getFrameDescriptorBuilder(), context.getSource());

        SchemeList localBindings = (SchemeList) letList.get(1);
        SchemeList body = letList.cdr().cdr();

        List<WriteLocalVariableExprNode> bindingExpressions = createWriteLocalVariables(localBindings, letContext, letrecCtx);
        List<SchemeExpression> bodyExpressions = TailCallUtil.convertBodyToSchemeExpressionsWithTCO(body, letContext, letrecCtx, CTX_BODY_INDEX);

        List<SchemeExpression> bindingsAndBodyExpressions = new ArrayList<>(bindingExpressions.size() + bodyExpressions.size());
        bindingsAndBodyExpressions.addAll(bindingExpressions);
        bindingsAndBodyExpressions.addAll(bodyExpressions);

        return SourceSectionUtil.setSourceSectionAndReturnExpr(new LetExprNode(bindingsAndBodyExpressions), letrecCtx);
    }

    // (letrec ((x 1) (y 2)) <body>)
    // 0  1         2
    private static List<WriteLocalVariableExprNode> createWriteLocalVariables(SchemeList localBindings, ParsingContext letContext, @Nullable ParserRuleContext letrecCtx) {
        List<WriteLocalVariableExprNode> result = new ArrayList<>();
        List<SchemeSymbol> symbols = new ArrayList<>();
        List<Object> dataExpressions = new ArrayList<>();
        for (Object obj : localBindings) {
            var bindingList = (SchemeCell) obj;
            var name = (SchemeSymbol) bindingList.get(0);
            letContext.findOrAddLocalSymbol(name);
            symbols.add(name);
            dataExpressions.add(bindingList.get(1));
        }

        letContext.makeLocalVariablesNullable(symbols);

        var letrecParamsCtx = letrecCtx != null ? (ParserRuleContext) letrecCtx.getChild(2).getChild(0) : null;
        for (int i = 0; i < symbols.size(); i++) {
            var identifierCtx = getIdentifierCtx(letrecParamsCtx, i);
            var exprCtx = getParameterExprCtx(letrecParamsCtx, i);
            var expression = InternalRepresentationConverter.convert(dataExpressions.get(i), letContext, false, false, exprCtx);
            result.add(CreateWriteExprNode.createWriteLocalVariableExprNode(symbols.get(i), expression, letContext, identifierCtx));
        }

        letContext.makeLocalVariablesNonNullable(symbols);

        return result;
    }

}
