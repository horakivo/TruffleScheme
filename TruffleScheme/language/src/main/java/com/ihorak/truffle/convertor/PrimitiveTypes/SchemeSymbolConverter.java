package com.ihorak.truffle.convertor.PrimitiveTypes;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.FrameIndexResult;
import com.ihorak.truffle.convertor.context.LexicalScope;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.scope.*;
import com.ihorak.truffle.type.SchemeSymbol;
import org.antlr.v4.runtime.Token;
import org.jetbrains.annotations.NotNull;

public class SchemeSymbolConverter {

    private SchemeSymbolConverter() {}

    public static SchemeExpression convert(SchemeSymbol symbol, ParsingContext context) {
        var indexPair = context.findClosureSymbol(symbol);
        if (indexPair != null) {
            if (indexPair.isLambdaParameter()) {
                return createReadProcedureArgExpr(indexPair);
            }
            return createReadVariableExpr(indexPair, symbol);
        } else {
            return new ReadGlobalVariableExprNode(symbol);
        }
    }

    public static SchemeExpression convert(SchemeSymbol symbol, ParsingContext context, Token symbolToken) {
        var expr = convert(symbol, context);
        SourceSectionUtil.setSourceSection(expr, symbolToken);

        return expr;
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

    private static SchemeExpression createReadProcedureArgExpr(@NotNull FrameIndexResult indexResult) {
        if (indexResult.lexicalScopeDepth() == 0) {
            return new ReadLocalProcedureArgExprNode(indexResult.index());
        }
        return new ReadNonLocalProcedureArgExprNode(indexResult.index(), indexResult.lexicalScopeDepth());
    }
}
