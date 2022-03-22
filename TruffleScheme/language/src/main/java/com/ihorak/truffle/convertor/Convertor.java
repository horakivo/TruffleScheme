package com.ihorak.truffle.convertor;

import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.parser.antlr.AntlrToProgram;
import com.ihorak.truffle.parser.antlr.R5RSLexer;
import com.ihorak.truffle.parser.antlr.R5RSParser;
import com.ihorak.truffle.parser.antlr.model.Program;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.ArrayList;
import java.util.List;

public class Convertor {


    public static List<SchemeExpression> convertToSchemeExpressions(CharStream charStream, ParsingContext globalContext) {

        var program = convertToInternalRepresentation(charStream);
        List<SchemeExpression> expressionList = new ArrayList<>();
        for (Object obj : program.getInternalRepresentations()) {
            expressionList.add(ListToExpressionConverter.convert(obj, globalContext));
        }

        return expressionList;
    }

    private static Program convertToInternalRepresentation(CharStream charStream) {
        R5RSLexer lexer = new R5RSLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        R5RSParser parser = new R5RSParser(tokens);

        var antlrParseTree = parser.prog();
        AntlrToProgram programVisitor = new AntlrToProgram();
        return programVisitor.visit(antlrParseTree);
    }
}
