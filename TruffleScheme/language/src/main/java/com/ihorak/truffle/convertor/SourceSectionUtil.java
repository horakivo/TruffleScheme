package com.ihorak.truffle.convertor;

import com.ihorak.truffle.node.SchemeExpression;
import org.antlr.v4.runtime.Token;

public class SourceSectionUtil {
    private SourceSectionUtil() {
    }

    public static void setSourceSection(SchemeExpression expression, Token token) {
        var startIndex = token.getStartIndex();
        var length = token.getText().length();

        expression.setSourceSection(startIndex, length);
    }

    public static void setSourceSection(SchemeExpression expression, Token startToken, Token stopToken) {
        var startIndex = startToken.getStartIndex();
        var stopIndex = stopToken.getStopIndex();
        var length = stopIndex - startIndex + 1;

        expression.setSourceSection(startIndex, length);
    }
}
