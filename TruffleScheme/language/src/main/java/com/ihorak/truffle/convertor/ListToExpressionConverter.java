package com.ihorak.truffle.convertor;

import com.ihorak.truffle.convertor.PrimitiveTypes.SchemeSymbolConverter;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.util.SpecialFormUtils;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.literals.BigIntLiteralNode;
import com.ihorak.truffle.node.literals.BooleanLiteralNode;
import com.ihorak.truffle.node.literals.DoubleLiteralNode;
import com.ihorak.truffle.node.literals.LongLiteralNode;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeSymbol;

import java.math.BigInteger;

public class ListToExpressionConverter {


    public static SchemeExpression convert(Object obj, ParsingContext context) {
        if (obj instanceof Long) {
            return convert((long) obj);
        } else if (obj instanceof SchemeSymbol schemeSymbol) {
            return SchemeSymbolConverter.convert(schemeSymbol, context);
        } else if (obj instanceof Boolean) {
            return convert((boolean) obj);
        } else if (obj instanceof SchemeCell schemeCell) {
            return convert(schemeCell, context);
        } else if (obj instanceof BigInteger bigInt) {
            return convert(bigInt);
        } else if (obj instanceof Double) {
            return convert((double) obj);
        } else {
            throw new IllegalArgumentException("ListToExpressionConverter: Unexpected type during conversion. Type: " + obj);

        }
    }


    private static SchemeExpression convert(long value) {
        return new LongLiteralNode(value);
    }

    private static SchemeExpression convert(BigInteger bigInteger) {
        return new BigIntLiteralNode(bigInteger);
    }

    private static SchemeExpression convert(double value) {
        return new DoubleLiteralNode(value);
    }

    private static SchemeExpression convert(boolean bool) {
        return new BooleanLiteralNode(bool);
    }


    private static SchemeExpression convert(SchemeCell list, ParsingContext context) {
        var firstElement = list.car;

        if (isSpecialForm(firstElement)) {
            return SpecialFormConverter.convertListToSpecialForm(list, context);
        } else if (isMacro(firstElement)) {
            return SchemeMacroConverter.convertMarco(list, context);
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

    private static boolean isLispMacro(Object firstElementOfList) {
        return firstElementOfList instanceof SchemeSymbol && firstElementOfList.equals(new SchemeSymbol("defmacro"));
    }
}
