package com.ihorak.truffle.convertor.special_form;

import com.ihorak.truffle.convertor.ConverterException;
import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.ConverterContext;
import com.ihorak.truffle.convertor.context.LexicalScope;
import com.ihorak.truffle.convertor.util.TailCallUtil;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNode;
import com.ihorak.truffle.node.special_form.LetExprNode;
import com.ihorak.truffle.runtime.SchemeList;
import com.ihorak.truffle.runtime.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractLetConverter {

    protected static final int CTX_PARAMS_OFFSET = 1;
    protected static final int CTX_BODY_INDEX = 3;


    protected static void validate(SchemeList letList) {
        var localBinding = letList.get(1);
        if (!(localBinding instanceof SchemeList localBindingList)) {
            throw new SchemeException("let: contract violation\nexpected: (let ((<symbol1>> <expr>>) .. (<symbol2> <expr>) ...) <>)", null);
        }

        List<SchemeSymbol> allIdentifiers = new ArrayList<>();
        for (Object obj : localBindingList) {
            if (!(obj instanceof SchemeList list)) {
                throw new SchemeException("let: contract violation\nexpected: (let ((id1 val-expr1) (id2 val-expr2) ...) body ...)", null);
            }

            if (list.size != 2) {
                throw new SchemeException("let: bad syntax\nexpected size of binding is 2\ngiven: " + list.size, null);
            }

            if (!(list.car instanceof SchemeSymbol symbol)) {
                throw new SchemeException("let: contract violation\nexpected: identifier\ngiven: " + list.car, null);
            }

            if (allIdentifiers.contains(symbol)) {
                throw new SchemeException("let: duplicate identifier " + symbol, null);
            } else {
                allIdentifiers.add(symbol);
            }
        }
    }

    protected static SchemeExpression createLetExpr(SchemeList letList, List<WriteLocalVariableExprNode> writeLocalsExprs, ConverterContext letContext, boolean isTailCallPosition, ConverterContext parentContext, @Nullable ParserRuleContext letCtx) {
        var bodyIR = letList.cdr.cdr;

        var bodyExprs = TailCallUtil.convertWithDefinitionsAndNoFrameCreation(bodyIR, letContext, isTailCallPosition, letCtx, CTX_BODY_INDEX);
        var allExprs = Stream.concat(writeLocalsExprs.stream(), bodyExprs.stream()).toList();

        propagateSelfTCOInfoToParentContext(letContext, parentContext);
        propagateClosureVariableUsageToParentFrame(letContext, parentContext);

        return SourceSectionUtil.setSourceSectionAndReturnExpr(new LetExprNode(allExprs), letCtx);
    }

    private static void propagateClosureVariableUsageToParentFrame(ConverterContext letConverterContext, ConverterContext parentFrame) {
        if (letConverterContext.isClosureVariablesUsed()) {
            if (parentFrame.getLexicalScope() == LexicalScope.LAMBDA || parentFrame.getLexicalScope() == LexicalScope.LET) {
                parentFrame.setClosureVariablesUsed(true);
            }
        }
    }


    private static void propagateSelfTCOInfoToParentContext(ConverterContext letContext, ConverterContext parentContext) {
        if (letContext.isFunctionSelfTailRecursive()) {
            parentContext.setFunctionAsSelfTailRecursive();
        }
    }


    protected static boolean isDefiningProcedureShadowed(ConverterContext letContext, List<SchemeSymbol> bindingSymbols) {
        var isProcedureBeingDefined = letContext.getFunctionDefinitionName().isPresent();
        if (isProcedureBeingDefined) {
            var name = letContext.getFunctionDefinitionName().get();
            return bindingSymbols.contains(name);
        }

        return false;
    }

    protected static ParserRuleContext getParameterExprCtx(@Nullable ParserRuleContext letParamCtx, int index) {
        if (letParamCtx == null) return null;

        var currentParamCtx = letParamCtx.getChild(index + CTX_PARAMS_OFFSET).getChild(0);
        return (ParserRuleContext) currentParamCtx.getChild(2);
    }

    protected static ParserRuleContext getIdentifierCtx(@Nullable ParserRuleContext letParamCtx, int index) {
        if (letParamCtx == null) return null;

        var currentParamCtx = letParamCtx.getChild(index + CTX_PARAMS_OFFSET).getChild(0);
        return (ParserRuleContext) currentParamCtx.getChild(1);
    }
}
