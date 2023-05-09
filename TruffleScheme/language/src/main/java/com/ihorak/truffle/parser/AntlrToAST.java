package com.ihorak.truffle.parser;

import com.ihorak.truffle.convertor.context.ConverterContext;
import com.ihorak.truffle.node.SchemeExpression;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.ArrayList;
import java.util.List;

public class AntlrToAST {

    private AntlrToAST() {
    }


    public static List<SchemeExpression> convert(CharStream charStream, ConverterContext globalConverterContext) {
        R5RSLexer lexer = new R5RSLexer(charStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(ParserErrorException.INSTANCE);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        R5RSParser parser = new R5RSParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(ParserErrorException.INSTANCE);

        var antlrToSchemeExpr = new AntlrToSchemeExpr(globalConverterContext);
        var progCtx = parser.prog();

        List<SchemeExpression> schemeExpressions = new ArrayList<>();
        for (int i = 0; i < progCtx.getChildCount() - 1; i++) {
            schemeExpressions.add(antlrToSchemeExpr.visit(progCtx.getChild(i)));
        }

        return schemeExpressions;
    }
}
