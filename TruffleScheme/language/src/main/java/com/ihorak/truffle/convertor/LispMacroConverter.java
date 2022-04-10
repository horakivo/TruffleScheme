package com.ihorak.truffle.convertor;

import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.ParserException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.literals.UndefinedLiteralNode;
import com.ihorak.truffle.node.macro.DefineMacroExprNode;
import com.ihorak.truffle.node.special_form.LambdaExprNode;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;

public class LispMacroConverter {

    public static SchemeExpression convertMacro(SchemeCell defmacroList, ParsingContext context) {
        if (defmacroList.size() == 3) {
            var macro = getMacro(defmacroList, context);
            var frame = Truffle.getRuntime().createVirtualFrame(new Object[]{}, FrameDescriptor.newBuilder().build());
            ParsingContext.macros.put(macro.getName(), macro.getTransformationProcedure().executeUserDefinedProcedure(frame));
            return new UndefinedLiteralNode();
        } else {
            throw new ParserException("define-macro: contract violation\nExpected three arguments\nGiven: " + defmacroList.size());
        }
    }

    private static DefineMacroExprNode getMacro(SchemeCell macroList, ParsingContext context) {
        var potentialName = macroList.get(1);
        if (potentialName instanceof SchemeSymbol) {
            var name = (SchemeSymbol) potentialName;
            var transformationProcedureExpr = ListToExpressionConverter.convert(macroList.get(2), context);
            if (transformationProcedureExpr instanceof LambdaExprNode) {
                return new DefineMacroExprNode(name, (LambdaExprNode) transformationProcedureExpr);
            }
            throw new ParserException("define-marco: contract violation\nExpected transformation procedure\nGiven: " + transformationProcedureExpr);

        } else {
            throw new ParserException("define-marco: expected identifier for the macro name.\nGiven: " + potentialName);
        }
    }
}
