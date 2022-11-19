package com.ihorak.truffle.convertor.PrimitiveTypes;

import com.ihorak.truffle.convertor.context.FrameIndexResult;
import com.ihorak.truffle.convertor.context.LexicalScope;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.scope.*;
import com.ihorak.truffle.type.SchemeSymbol;
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
            return ReadGlobalVariableExprNodeGen.create(symbol);
        }
    }

    private static SchemeExpression createReadVariableExpr(FrameIndexResult indexFrameIndexResult, SchemeSymbol symbol) {
        if (indexFrameIndexResult.lexicalScopeDepth() == 0) {
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
