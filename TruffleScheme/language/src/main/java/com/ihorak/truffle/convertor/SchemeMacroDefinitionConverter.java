package com.ihorak.truffle.convertor;

import com.ihorak.truffle.convertor.context.LexicalScope;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.util.CreateWriteExprNode;
import com.ihorak.truffle.exceptions.ParserException;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.macro.DefineMacroExprNode;
import com.ihorak.truffle.node.special_form.LambdaExprNode;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

public class SchemeMacroDefinitionConverter {

    private static final int CTX_IDENTIFIER = 2;
    private static final int CTX_TRANSFORMATION_BODY = 3;

    //TODO podivat se jestli tohle je potreba
    private SchemeMacroDefinitionConverter() {
    }

    public static SchemeExpression convertMarco(SchemeList macroList, ParsingContext context, @Nullable ParserRuleContext macroCtx) {
        validate(macroList);

        var name = (SchemeSymbol) macroList.get(1);
        var transformationProcedureCtx = macroCtx != null ? (ParserRuleContext) macroCtx.getChild(CTX_TRANSFORMATION_BODY) : null;
        var transformationProcedureExpr = InternalRepresentationConverter.convert(macroList.get(2), context, false, false, transformationProcedureCtx);

        if (!(transformationProcedureExpr instanceof LambdaExprNode lambdaExprNode)) {
            throw new ParserException("define-marco: contract violation\nExpected: <procedure>\nGiven: " + transformationProcedureExpr);
        }

        context.addMacro(name, lambdaExprNode.callTarget);
        var symbolCtx = macroCtx != null ? (ParserRuleContext) macroCtx.getChild(CTX_IDENTIFIER) : null;
        if (context.getLexicalScope() == LexicalScope.GLOBAL) {
            return CreateWriteExprNode.createWriteGlobalVariableExprNode(name, new DefineMacroExprNode(lambdaExprNode), symbolCtx);
        } else {
            return CreateWriteExprNode.createWriteLocalVariableExprNode(name, new DefineMacroExprNode(lambdaExprNode), context, symbolCtx);
        }


    }


    private static void validate(SchemeList macroList) {
        if (macroList.size != 3) {
            throw new SchemeException("define-macro: contract violation. Wrong number of arguments\nExpected: 3 \nGiven: " + macroList.size, null);
        }

        if (!(macroList.get(1) instanceof SchemeSymbol)) {
            throw new SchemeException("define-macro: contract violation. No identifier\nExpected: identifier \nGiven: " + macroList.get(1), null);
        }
    }
}
