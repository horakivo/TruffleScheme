package com.ihorak.truffle.convertor;

import com.ihorak.truffle.node.SchemeExpression;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.jetbrains.annotations.NotNull;

public class SourceSectionUtil {
    private SourceSectionUtil() {
    }

    public static void setSourceSection(@NotNull SchemeExpression expression, @NotNull Token token) {
        var startIndex = token.getStartIndex();
        var length = token.getText().length();

        expression.setSourceSection(startIndex, length);
    }

    public static void setSourceSection(@NotNull SchemeExpression expression, @NotNull ParserRuleContext ctx) {
        var startToken = ctx.start;
        var stopToken = ctx.stop;
        var length = stopToken.getStopIndex() - startToken.getStartIndex() + 1;

        expression.setSourceSection(startToken.getStartIndex(), length);
    }

    public static SchemeExpression setSourceSectionAndReturnExpr(@NotNull SchemeExpression expression, @NotNull ParserRuleContext ctx) {
        var startToken = ctx.start;
        var stopToken = ctx.stop;
        var length = stopToken.getStopIndex() - startToken.getStartIndex() + 1;

        expression.setSourceSection(startToken.getStartIndex(), length);

        return expression;
    }

    public static void setSourceSection(@NotNull SchemeExpression expression,@NotNull Token startToken,@NotNull Token stopToken) {
        var startIndex = startToken.getStartIndex();
        var stopIndex = stopToken.getStopIndex();
        var length = stopIndex - startIndex + 1;

        expression.setSourceSection(startIndex, length);
    }
}
