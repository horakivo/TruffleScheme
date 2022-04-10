package com.ihorak.truffle.convertor;

import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.context.LexicalScope;
import com.ihorak.truffle.exceptions.ParserException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.macro.DefineMacroExprNode;
import com.ihorak.truffle.node.scope.WriteGlobalVariableExprNodeGen;
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNodeGen;
import com.ihorak.truffle.node.special_form.LambdaExprNode;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeSymbol;

public class SchemeMacroConverter {

    public static SchemeExpression convertMarco(SchemeCell macroList, ParsingContext context) {
        if (macroList.size() == 3) {
            var macro = getMacro(macroList, context);

            if (context.getLexicalScope() == LexicalScope.GLOBAL) {
                return WriteGlobalVariableExprNodeGen.create(macro.getName(), macro);
            } else {
                return createWriteLocalVariable(context, macro);
            }
        } else {
            throw new ParserException("define-macro: contract violation\nExpected three arguments\nGiven: " + macroList.size());
        }
    }

    private static SchemeExpression createWriteLocalVariable(ParsingContext context, DefineMacroExprNode macro) {
        var index = context.findLocalSymbol(macro.getName());
        if (index == null) {
            index = context.addLocalSymbol(macro.getName());
        }
        return WriteLocalVariableExprNodeGen.create(index, macro.getName(), macro);
    }

    private static DefineMacroExprNode getMacro(SchemeCell macroList, ParsingContext context) {
        var potentialName = macroList.get(1);
        if (potentialName instanceof SchemeSymbol) {
            var name = (SchemeSymbol) potentialName;
            var transformationProcedureExpr = ListToExpressionConverter.convert(macroList.get(2), context);
            if (transformationProcedureExpr instanceof LambdaExprNode) {
                return new DefineMacroExprNode(name, (LambdaExprNode) transformationProcedureExpr);
            }
            throw new ParserException("define-marco: contract violation\nExpected lambda expression\nGiven: " + transformationProcedureExpr);
        } else {
            throw new ParserException("define-marco: expected identifier for the macro name.\nGiven: " + potentialName);
        }
    }
}
