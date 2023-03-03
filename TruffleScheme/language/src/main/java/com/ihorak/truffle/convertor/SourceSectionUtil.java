package com.ihorak.truffle.convertor;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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

    public static void setSourceSection(@NotNull SchemeExpression expression, @NotNull Token startToken, @NotNull Token stopToken) {
        var startIndex = startToken.getStartIndex();
        var stopIndex = stopToken.getStopIndex();
        var length = stopIndex - startIndex + 1;

        expression.setSourceSection(startIndex, length);
    }

    public static SourceSection createSourceSection(List<SchemeExpression> exprs, Source source) {
        if (exprs.isEmpty()) return source.createSection(0, 0);
        var firstExpr = exprs.get(0);
        var lastExpr = exprs.get(exprs.size() - 1);

        var startIndex = firstExpr.getSourceStartIndex();
        var endIndex = lastExpr.getSourceEndIndex() - startIndex;
        return source.createSection(startIndex, endIndex);
    }

}
