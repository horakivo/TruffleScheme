package com.ihorak.truffle.parser.parser;

import com.ihorak.truffle.parser.antlr.R5RSLexer;
import com.ihorak.truffle.parser.antlr.R5RSParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.List;

public class Parser {

    public static List<Object> parse(CharStream charStream) {
        R5RSLexer lexer = new R5RSLexer(charStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(ParserErrorException.INSTANCE);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        R5RSParser parser = new R5RSParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(ParserErrorException.INSTANCE);

        var antlrParseTree = parser.prog();
        AntlrToProgram programVisitor = new AntlrToProgram();
        return programVisitor.visit(antlrParseTree).getInternalRepresentations();
    }
}
