package com.ihorak.truffle.parser;

import com.ihorak.truffle.context.Context;
import com.ihorak.truffle.context.Mode;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.literals.*;
import com.ihorak.truffle.node.special_form.lambda.ReadGlobalVariableExprNodeGen;
import com.ihorak.truffle.node.special_form.lambda.ReadLocalVariableExprNodeGen;
import com.ihorak.truffle.parser.Util.SpecialFormUtils;
import com.ihorak.truffle.type.*;

import java.math.BigInteger;

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
        } else if (obj instanceof BigInteger) {
            return convert((BigInteger) obj);
        } else if (obj instanceof Double) {
            return convert((double) obj);
        } else {
            throw new IllegalArgumentException("ListToExpressionConverter: Unexpected type during conversion. Type: " + obj);

        }
    }


    private static SchemeExpression convert(long schemeLong) {
        return new LongLiteralNode(schemeLong);
    }

    private static SchemeExpression convert(BigInteger bigInteger) {
        return new BigIntLiteralNode(bigInteger);
    }

    private static SchemeExpression convert(double schemeDouble) {
        return new DoubleLiteralNode(schemeDouble);
    }

    private static SchemeExpression convert(boolean schemeBoolean) {
        return new BooleanLiteralNode(schemeBoolean);
    }

    private static SchemeExpression convert(SchemeSymbol symbol, Context context) {
        if (context.getMode() == Mode.RUN_TIME) {
            return handleSymbolInRuntime(symbol);
        } else {
            return handleSymbolInParseTime(symbol, context);
        }
    }

    private static SchemeExpression handleSymbolInRuntime(SchemeSymbol symbol) {
        return SymbolExprNodeGen.create(symbol);
    }

    private static SchemeExpression handleSymbolInParseTime(SchemeSymbol symbol, Context context) {
        var indexPair = context.findSymbol(symbol);
        if (indexPair != null) {
            return ReadLocalVariableExprNodeGen.create(symbol, indexPair.getFrameIndex(), indexPair.getLexicalScopeDepth());
        } else {
            //the variable was not define yet, therefore it will be defined later in global env (can't be defined somewhere in local environment because then we would have parse it )
            var index = context.addGlobalSymbol(symbol);
            return ReadGlobalVariableExprNodeGen.create(symbol, index);
        }
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
        return firstElementOfList instanceof SchemeSymbol && firstElementOfList.equals(new SchemeSymbol("define-macro"));
    }
}
