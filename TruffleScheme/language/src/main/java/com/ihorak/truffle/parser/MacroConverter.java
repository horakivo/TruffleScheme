package com.ihorak.truffle.parser;

import com.ihorak.truffle.context.Context;
import com.ihorak.truffle.exceptions.ParserException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.macro.DefineMacroExprNode;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeSymbol;

public class MacroConverter {

    public static SchemeExpression convertMarco(SchemeCell macroList, Context context) {
        if (macroList.size() == 3) {
            var potentialName = macroList.get(1);
            if (potentialName instanceof SchemeSymbol) {
                var name = (SchemeSymbol) potentialName;
                var transformationProcedureExpr = ListToExpressionConverter.convert(macroList.get(2), context);
                return new DefineMacroExprNode(name, transformationProcedureExpr);
            } else {
                throw new ParserException("define-marco: expected identifier for the macro name.\nGiven: " + potentialName);
            }
        } else {
            throw new ParserException("define-macro: contract violation\nExpected three arguments\nGiven: " + macroList.size());
        }
    }
}
