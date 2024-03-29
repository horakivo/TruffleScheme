package com.ihorak.truffle.convertor.special_form;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.context.ConverterContext;
import com.ihorak.truffle.convertor.util.CreateWriteExprNode;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNode;
import com.ihorak.truffle.runtime.SchemeList;
import com.ihorak.truffle.runtime.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LetrecConverter extends AbstractLetConverter {

    private LetrecConverter() {
    }

    public static SchemeExpression convert(SchemeList letrecList, ConverterContext context, boolean isTailCallPosition, @Nullable ParserRuleContext letCtx) {
        validate(letrecList);
        ConverterContext letContext = ConverterContext.createLetContext(context);

        var localBindingsIR = (SchemeList) letrecList.get(1);

        var writeLocalsExprs = createWriteLocalVariables(localBindingsIR, letContext, letCtx);
        return createLetExpr(letrecList, writeLocalsExprs, letContext, isTailCallPosition, context, letCtx);
    }

    // (letrec ((x 1) (y 2)) <body>)
    // 0  1         2
    private static List<WriteLocalVariableExprNode> createWriteLocalVariables(SchemeList localBindings, ConverterContext letrecContext, @Nullable ParserRuleContext letrecCtx) {
        List<WriteLocalVariableExprNode> result = new ArrayList<>();
        List<SchemeSymbol> symbols = new ArrayList<>();
        List<Object> dataExpressions = new ArrayList<>();
        for (Object obj : localBindings) {
            var bindingList = (SchemeList) obj;
            var name = (SchemeSymbol) bindingList.get(0);
            letrecContext.findOrAddLocalSymbol(name);
            symbols.add(name);
            dataExpressions.add(bindingList.get(1));
        }

        letrecContext.makeLocalVariablesNullable(symbols);

        var letrecParamsCtx = letrecCtx != null ? (ParserRuleContext) letrecCtx.getChild(2).getChild(0) : null;
        for (int i = 0; i < symbols.size(); i++) {
            var identifierCtx = getIdentifierCtx(letrecParamsCtx, i);
            var exprCtx = getParameterExprCtx(letrecParamsCtx, i);
            var expressionIR = dataExpressions.get(i);
            SchemeExpression expression;
            if (isLambda(expressionIR)) {
                var name = symbols.get(i);
                var lambdaCtx = exprCtx != null ? (ParserRuleContext) exprCtx.getChild(0) : null;
                expression = LambdaConverter.convert((SchemeList) expressionIR, letrecContext, name, lambdaCtx);
            } else {
                expression = InternalRepresentationConverter.convert(expressionIR, letrecContext, false, false, exprCtx);
            }
            result.add(CreateWriteExprNode.createWriteLocalVariableExprNode(symbols.get(i), expression, letrecContext, identifierCtx));
        }

        letrecContext.makeLocalVariablesNonNullable(symbols);

        if (isDefiningProcedureShadowed(letrecContext, symbols)) {
            letrecContext.markDefiningFunctionAsShadowed();
        }

        return result;
    }

    private static boolean isLambda(Object expressionIR) {
        return expressionIR instanceof SchemeList list &&
                !list.isEmpty &&
                list.get(0) instanceof SchemeSymbol symbol &&
                symbol.value().equals("lambda");
    }

}
