package com.ihorak.truffle.convertor.PrimitiveTypes;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.FrameIndexResult;
import com.ihorak.truffle.convertor.context.LexicalScope;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.scope.*;
import com.ihorak.truffle.type.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SchemeSymbolConverter {

    private SchemeSymbolConverter() {
    }

    public static SchemeExpression convert(SchemeSymbol symbol, ParsingContext context, @Nullable ParserRuleContext ctx) {
        var indexPair = context.findClosureSymbol(symbol);
        var expr = indexPair == null ? new ReadGlobalVariableExprNode(symbol) : createReadVariableExpr(indexPair, symbol);
        return SourceSectionUtil.setSourceSectionAndReturnExpr(expr, ctx);
    }

    private static SchemeExpression createReadVariableExpr(@NotNull FrameIndexResult indexFrameIndexResult, SchemeSymbol symbol) {
        /*
         * As far as I know nullable arguments are problematic only as local variables because the value then can be NULL.
         * There is no other way how to delay the execution then lambda expr. Look at tests {@link DefineExprNodeTest}
         *
         * */
        if (indexFrameIndexResult.lexicalScopeDepth() == 0) {
            if (indexFrameIndexResult.isNullable()) {
                return ReadLocalNullableVariableExprNodeGen.create(indexFrameIndexResult.index(), symbol);
            }
            return ReadLocalVariableExprNodeGen.create(indexFrameIndexResult.index(), symbol);
        }
        return ReadNonLocalVariableExprNodeGen.create(indexFrameIndexResult.lexicalScopeDepth(), indexFrameIndexResult.index(), symbol);
    }
}
