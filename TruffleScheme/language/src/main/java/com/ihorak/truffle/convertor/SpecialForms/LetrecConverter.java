package com.ihorak.truffle.convertor.SpecialForms;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
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

import java.util.ArrayList;
import java.util.List;

public class LetrecConverter extends AbstractLetConverter {

    private LetrecConverter() {
    }

    //TODO solve code duplication
    public static LetExprNode convert(SchemeList letList, ParsingContext context, ParserRuleContext letrecCtx) {
        validate(letList);
        ParsingContext letContext = new ParsingContext(context, LexicalScope.LETREC, context.getFrameDescriptorBuilder());

        SchemeList localBindings = (SchemeList) letList.get(1);
        SchemeList body = letList.cdr().cdr();

        List<WriteLocalVariableExprNode> bindingExpressions = createWriteLocalVariables(localBindings, letContext, letrecCtx);
        List<SchemeExpression> bodyExpressions = TailCallUtil.convertBodyToSchemeExpressionsWithTCO(body, letContext, letrecCtx, CTX_BODY_INDEX);

        List<SchemeExpression> bindingsAndBodyExpressions = new ArrayList<>(bindingExpressions.size() + bodyExpressions.size());
        bindingsAndBodyExpressions.addAll(bindingExpressions);
        bindingsAndBodyExpressions.addAll(bodyExpressions);

        return new LetExprNode(bindingsAndBodyExpressions);
    }

    // (letrec ((x 1) (y 2)) <body>)
    // 0  1         2
    private static List<WriteLocalVariableExprNode> createWriteLocalVariables(SchemeList localBindings, ParsingContext letContext, ParserRuleContext letrecCtx) {
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

        var letrecParamsCtx = (ParserRuleContext) letrecCtx.getChild(2).getChild(0);
        for (int i = 0; i < symbols.size(); i++) {
            var currentParamCtx = letrecParamsCtx.getChild(i + CTX_PARAMS_OFFSET).getChild(0);
            var identifierCtx = (ParserRuleContext)currentParamCtx.getChild(1);
            var exprCtx = (ParserRuleContext)currentParamCtx.getChild(2);
            var expression = InternalRepresentationConverter.convert(dataExpressions.get(i), letContext, false, false, exprCtx);
            result.add(CreateWriteExprNode.createWriteLocalVariableExprNode(symbols.get(i), expression, letContext, identifierCtx));
        }

        letContext.makeLocalVariablesNonNullable(symbols);

        return result;
    }

}
