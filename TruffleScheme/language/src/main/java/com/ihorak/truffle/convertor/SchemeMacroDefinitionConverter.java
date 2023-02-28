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

public class SchemeMacroDefinitionConverter {

    private static final int CTX_IDENTIFIER = 2;

    private SchemeMacroDefinitionConverter() {
    }

    public static SchemeExpression convertMarco(SchemeList macroList, ParsingContext context, ParserRuleContext macroCtx) {
        validate(macroList);

        var name = (SchemeSymbol) macroList.get(1);
        var transformationProcedureExpr = InternalRepresentationConverter.convert(macroList.get(2), context, false, false);

        if (!(transformationProcedureExpr instanceof LambdaExprNode lambdaExprNode)) {
            throw new ParserException("define-marco: contract violation\nExpected: <procedure>\nGiven: " + transformationProcedureExpr);
        }

        context.addMacro(name);
        var symbolCtx = (ParserRuleContext) macroCtx.getChild(CTX_IDENTIFIER);
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
