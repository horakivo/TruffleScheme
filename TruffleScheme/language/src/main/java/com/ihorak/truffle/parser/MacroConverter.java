package com.ihorak.truffle.parser;

import com.ihorak.truffle.context.Context;
import com.ihorak.truffle.context.LexicalScope;
import com.ihorak.truffle.exceptions.ParserException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.macro.DefineMacroExprNode;
import com.ihorak.truffle.node.special_form.lambda.WriteGlobalRuntimeVariableExprNodeGen;
import com.ihorak.truffle.node.special_form.lambda.WriteLocalVariableExprNodeGen;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeSymbol;

public class MacroConverter {

    public static SchemeExpression convertMarco(SchemeCell macroList, Context context) {
        if (macroList.size() == 3) {
            var macro = getMacro(macroList, context);

            if (context.getLexicalScope() == LexicalScope.GLOBAL) {
                return WriteGlobalRuntimeVariableExprNodeGen.create(macro.getName(), macro);
            } else {
                return createWriteLocalVariable(context, macro);
            }
        } else {
            throw new ParserException("define-macro: contract violation\nExpected three arguments\nGiven: " + macroList.size());
        }
    }

    private static SchemeExpression createWriteLocalVariable(Context context, DefineMacroExprNode macro) {
        var index = context.findLocalSymbol(macro.getName());
        if (index == null) {
            index = context.addLocalSymbol(macro.getName());
        }
        return WriteLocalVariableExprNodeGen.create(index, macro.getName(), macro);
    }

    private static DefineMacroExprNode getMacro(SchemeCell macroList, Context context) {
        var potentialName = macroList.get(1);
        if (potentialName instanceof SchemeSymbol) {
            var name = (SchemeSymbol) potentialName;
            var transformationProcedureExpr = ListToExpressionConverter.convert(macroList.get(2), context);
            return new DefineMacroExprNode(name, transformationProcedureExpr);
        } else {
            throw new ParserException("define-marco: expected identifier for the macro name.\nGiven: " + potentialName);
        }
    }
}
