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
import java.util.stream.Stream;

public class LetrecConverter extends AbstractLetConverter {

    private LetrecConverter() {
    }

    public static SchemeExpression convert(SchemeList letList, ParsingContext context, @Nullable ParserRuleContext letCtx) {
        validate(letList);
        ParsingContext letContext = new ParsingContext(context, LexicalScope.LET, context.getFrameDescriptorBuilder(), context.getSource());

        var localBindingsIR = (SchemeList) letList.get(1);
        var bodyIR = letList.cdr().cdr();

        var writeLocalVariableExpr = createWriteLocalVariables(localBindingsIR, letContext, letCtx);
        var bodyExprs = TailCallUtil.convertBodyToSchemeExpressionsWithTCO(bodyIR, letContext, letCtx, CTX_BODY_INDEX);
        var allExprs = Stream.concat(writeLocalVariableExpr.stream(), bodyExprs.stream()).toList();

        if (letContext.isTailCallProcedureBeingDefined()) {
            // we need to set the parent context also as tail
            context.setDefiningProcedureAsTailCall();
        }

        return SourceSectionUtil.setSourceSectionAndReturnExpr(new LetExprNode(allExprs), letCtx);
    }

    // (letrec ((x 1) (y 2)) <body>)
    // 0  1         2
    private static List<WriteLocalVariableExprNode> createWriteLocalVariables(SchemeList localBindings, ParsingContext letContext, @Nullable ParserRuleContext letrecCtx) {
        List<WriteLocalVariableExprNode> result = new ArrayList<>();
        List<SchemeSymbol> symbols = new ArrayList<>();
        List<Object> dataExpressions = new ArrayList<>();
        for (Object obj : localBindings) {
            var bindingList = (SchemeList) obj;
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
            var expressionIR = dataExpressions.get(i);
            SchemeExpression expression;
            if (isLambda(expressionIR)) {
                var name = symbols.get(i);
                var lambdaCtx = exprCtx != null ? (ParserRuleContext) exprCtx.getChild(0) : null;
                expression = LambdaConverter.convert((SchemeList) expressionIR, letContext, name, lambdaCtx);
            } else {
                expression = InternalRepresentationConverter.convert(expressionIR, letContext, false, false, exprCtx);
            }
            result.add(CreateWriteExprNode.createWriteLocalVariableExprNode(symbols.get(i), expression, letContext, identifierCtx));
        }

        letContext.makeLocalVariablesNonNullable(symbols);

        return result;
    }

    private static boolean isLambda(Object expressionIR) {
        return expressionIR instanceof SchemeList list &&
                !list.isEmpty &&
                list.get(0) instanceof SchemeSymbol symbol &&
                symbol.getValue().equals("lambda");
    }

}
