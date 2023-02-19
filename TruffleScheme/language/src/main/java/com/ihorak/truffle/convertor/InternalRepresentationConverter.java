package com.ihorak.truffle.convertor;

import com.ihorak.truffle.convertor.PrimitiveTypes.*;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.strings.TruffleString;

import java.math.BigInteger;

public class InternalRepresentationConverter {

    private InternalRepresentationConverter() {}

    public static SchemeExpression convert(Object obj, ParsingContext context, boolean isTailCall, boolean isDefinitionAllowed) {
        if (obj instanceof Long longValue) {
            return LongConverter.convert(longValue);
        } else if (obj instanceof SchemeSymbol schemeSymbol) {
            return SchemeSymbolConverter.convert(schemeSymbol, context);
        } else if (obj instanceof Boolean bool) {
            return BooleanConverter.convert(bool);
        } else if (obj instanceof SchemeList schemeList) {
            return SchemeCellConverter.convert(schemeList, context, isTailCall, isDefinitionAllowed);
        } else if (obj instanceof BigInteger bigInt) {
            return BigIntConverter.convert(bigInt);
        } else if (obj instanceof Double doubleValue) {
            return DoubleConverter.convert(doubleValue);
        } else if (obj instanceof TruffleString truffleString) {
            return TruffleStringConverter.convert(truffleString);
        } else {
            throw new IllegalArgumentException("ListToExpressionConverter: Unexpected type during conversion. Type: " + obj);
        }
    }



}
