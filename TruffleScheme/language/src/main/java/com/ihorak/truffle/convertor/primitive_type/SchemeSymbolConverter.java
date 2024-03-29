package com.ihorak.truffle.convertor.primitive_type;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.FrameIndexResult;
import com.ihorak.truffle.convertor.context.ConverterContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.scope.*;
import com.ihorak.truffle.runtime.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SchemeSymbolConverter {

    private SchemeSymbolConverter() {
    }

    public static SchemeExpression convert(SchemeSymbol symbol, ConverterContext context, @Nullable ParserRuleContext ctx) {
        var indexPair = context.findClosureSymbol(symbol);
        var expr = indexPair == null ? ReadGlobalVariableExprNodeGen.create(symbol) : createReadVariableExpr(indexPair, symbol);
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
