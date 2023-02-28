package com.ihorak.truffle.convertor;

import com.ihorak.truffle.convertor.PrimitiveTypes.*;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.parser.R5RSParser;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.strings.TruffleString;
import org.antlr.v4.runtime.ParserRuleContext;

import java.math.BigInteger;

public class InternalRepresentationConverter {

    private InternalRepresentationConverter() {}

    public static SchemeExpression convert(Object obj, ParsingContext context, boolean isTailCall, boolean isDefinitionAllowed) {
        if (obj instanceof Long longValue) {
            return LongConverter.convert(longValue, null);
        } else if (obj instanceof SchemeSymbol schemeSymbol) {
            return SchemeSymbolConverter.convert(schemeSymbol, context, null);
        } else if (obj instanceof Boolean bool) {
            return BooleanConverter.convert(bool, null);
        } else if (obj instanceof SchemeList schemeList) {
            return SchemeListConverter.convert(schemeList, context, isTailCall, isDefinitionAllowed, null);
        } else if (obj instanceof BigInteger bigInt) {
            return BigIntConverter.convert(bigInt, null);
        } else if (obj instanceof Double doubleValue) {
            return DoubleConverter.convert(doubleValue, null);
        } else if (obj instanceof TruffleString truffleString) {
            return TruffleStringConverter.convert(truffleString, null);
        } else {
            throw new IllegalArgumentException("ListToExpressionConverter: Unexpected type during conversion. Type: " + obj);
        }
    }

    public static SchemeExpression convert(Object obj, ParsingContext context, boolean isTailCall, boolean isDefinitionAllowed, ParserRuleContext ctx) {
        if (obj instanceof Long longValue) {
            return LongConverter.convert(longValue, ctx);
        } else if (obj instanceof SchemeSymbol schemeSymbol) {
            return SchemeSymbolConverter.convert(schemeSymbol, context, ctx);
        } else if (obj instanceof Boolean bool) {
            return BooleanConverter.convert(bool, ctx);
        } else if (obj instanceof SchemeList schemeList) {
            return SchemeListConverter.convert(schemeList, context, isTailCall, isDefinitionAllowed, ctx);
        } else if (obj instanceof BigInteger bigInt) {
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
