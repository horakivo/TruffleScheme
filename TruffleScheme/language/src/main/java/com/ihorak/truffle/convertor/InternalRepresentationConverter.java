package com.ihorak.truffle.convertor;

import com.ihorak.truffle.convertor.PrimitiveTypes.*;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeSymbol;

import java.math.BigInteger;

public class InternalRepresentationConverter {

    private InternalRepresentationConverter() {}

    public static SchemeExpression convert(Object obj, ParsingContext context, boolean isTailCall) {
        if (obj instanceof Long longValue) {
            return LongConverter.convert(longValue);
        } else if (obj instanceof SchemeSymbol schemeSymbol) {
            return SchemeSymbolConverter.convert(schemeSymbol, context);
        } else if (obj instanceof Boolean bool) {
            return BooleanConverter.convert(bool);
        } else if (obj instanceof SchemeCell schemeCell) {
            return SchemeCellConverter.convert(schemeCell, context, isTailCall);
        } else if (obj instanceof BigInteger bigInt) {
            return BigIntConverter.convert(bigInt);
        } else if (obj instanceof Double doubleValue) {
            return DoubleConverter.convert(doubleValue);
        } else {
            throw new IllegalArgumentException("ListToExpressionConverter: Unexpected type during conversion. Type: " + obj);
        }
    }



}
