package com.ihorak.truffle.convertor;

import com.ihorak.truffle.convertor.primitive_type.*;
import com.ihorak.truffle.convertor.context.ConverterContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.runtime.SchemeBigInt;
import com.ihorak.truffle.runtime.SchemeList;
import com.ihorak.truffle.runtime.SchemeSymbol;
import com.oracle.truffle.api.strings.TruffleString;
import org.antlr.v4.runtime.ParserRuleContext;


public class InternalRepresentationConverter {

    private InternalRepresentationConverter() {}

    public static SchemeExpression convert(Object obj, ConverterContext context, boolean isTailCall, boolean isDefinitionAllowed, ParserRuleContext ctx) {
        if (obj instanceof Long longValue) {
            return LongConverter.convert(longValue, ctx);
        } else if (obj instanceof SchemeSymbol schemeSymbol) {
            return SchemeSymbolConverter.convert(schemeSymbol, context, ctx);
        } else if (obj instanceof Boolean bool) {
            return BooleanConverter.convert(bool, ctx);
        } else if (obj instanceof SchemeList schemeList) {
            return SchemeListConverter.convert(schemeList, context, isTailCall, isDefinitionAllowed, ctx);
        } else if (obj instanceof SchemeBigInt bigInt) {
            return BigIntConverter.convert(bigInt, ctx);
        } else if (obj instanceof Double doubleValue) {
            return DoubleConverter.convert(doubleValue, ctx);
        } else if (obj instanceof TruffleString truffleString) {
            return TruffleStringConverter.convert(truffleString, ctx);
        } else {
            throw new IllegalArgumentException("ListToExpressionConverter: Unexpected type during conversion. Type: " + obj);
        }
    }
}
