package com.ihorak.truffle.parser;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.literals.BooleanLiteralNode;
import com.ihorak.truffle.node.literals.LongLiteralNode;
import com.ihorak.truffle.node.literals.SymbolExprNodeGen;
import com.ihorak.truffle.node.special_form.lambda.ReadLocalVariableExprNodeGen;
import com.ihorak.truffle.parser.Util.SpecialFormUtils;
import com.ihorak.truffle.type.*;

public class ListToExpressionConverter {


    public static SchemeExpression convert(Object obj, Context context) {
        if (obj instanceof Long) {
            return convert((long) obj);
        } else if (obj instanceof SchemeSymbol) {
            return convert((SchemeSymbol) obj, context);
        } else if (obj instanceof Boolean) {
            return convert((boolean) obj);
        } else if (obj instanceof SchemeCell) {
            return convert((SchemeCell) obj, context);
        } else {
            throw new IllegalArgumentException("ListToExpressionConverter: Unexpected type during conversion. Type: " + obj);
        }
    }


    private static SchemeExpression convert(long schemeLong) {
        return new LongLiteralNode(schemeLong);
    }

    private static SchemeExpression convert(boolean schemeBoolean) {
        return new BooleanLiteralNode(schemeBoolean);
    }

    private static SchemeExpression convert(SchemeSymbol symbol, Context context) {
        if (isLocalScope(context, symbol)) {
            var symbolResult = context.findSymbol(symbol);
            if (symbolResult != null) {
                return ReadLocalVariableExprNodeGen.create(symbol, symbolResult.getFrameIndex(), symbolResult.getLexicalScopeDepth());
            }
        }
        //is not local variable
        return SymbolExprNodeGen.create(symbol);
    }

    private static boolean isLocalScope(Context context, SchemeSymbol symbol) {
        if (context.getLexicalScope() == Context.LexicalScope.LAMBDA || context.getLexicalScope() == Context.LexicalScope.LET) {
            return context.getLocalVariables().contains(symbol);
        }
        return false;
    }

    private static SchemeExpression convert(SchemeCell list, Context context) {
        var firstElement = list.car;

        if (isSpecialForm(firstElement)) {
            return SpecialFormConverter.convertListToSpecialForm(list, context);
        } else if (isMacro(firstElement)) {
            return MacroConverter.convertMarco(list, context);
        } else {
            return ProcedureCallConverter.convertListToProcedureCall(list, context);
        }

    }

    private static boolean isSpecialForm(Object firstElementOfList) {
        return firstElementOfList instanceof SchemeSymbol && SpecialFormUtils.isSpecialForm((SchemeSymbol) firstElementOfList);
    }

    private static boolean isMacro(Object firstElementOfList) {
        return firstElementOfList instanceof SchemeSymbol && ((SchemeSymbol) firstElementOfList).equals(new SchemeSymbol("define-macro"));
    }
}
