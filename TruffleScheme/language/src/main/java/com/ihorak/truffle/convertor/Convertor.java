package com.ihorak.truffle.convertor;

import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.parser.parser.Parser;
import org.antlr.v4.runtime.CharStream;

import java.util.ArrayList;
import java.util.List;

public class Convertor {


    public static List<SchemeExpression> convertToSchemeExpressions(CharStream charStream, ParsingContext globalContext) {

        var internalRepresentation = Parser.parse(charStream);
        List<SchemeExpression> result = new ArrayList<>();
        for (Object obj : internalRepresentation) {
            result.add(ListToExpressionConverter.convert(obj, globalContext));
        }

        return result;
    }
}
