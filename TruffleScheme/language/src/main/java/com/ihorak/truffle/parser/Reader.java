package com.ihorak.truffle.parser;

import com.ihorak.truffle.context.Context;
import com.ihorak.truffle.context.Mode;
import com.ihorak.truffle.node.ProcedureRootNode;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.SchemeRootNode;
import com.ihorak.truffle.parser.antlr.AntlrToProgram;
import com.ihorak.truffle.parser.antlr.R5RSLexer;
import com.ihorak.truffle.parser.antlr.R5RSParser;
import com.ihorak.truffle.parser.antlr.model.Program;
import com.oracle.truffle.api.frame.FrameDescriptor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Reader {




    public static List<SchemeExpression> parseProgram(CharStream charStream, Context globalContext) {

        var program = parseProgram(charStream);
        List<SchemeExpression> expressionList = new ArrayList<>();
        for (Object obj : program.getInternalRepresentations()) {
            expressionList.add(ListToExpressionConverter.convert(obj, globalContext));
        }

        return expressionList;
    }

    private static Program parseProgram(CharStream charStream) {
        R5RSLexer lexer = new R5RSLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        R5RSParser parser = new R5RSParser(tokens);

        var antlrParseTree = parser.prog();
        AntlrToProgram programVisitor = new AntlrToProgram();
        return programVisitor.visit(antlrParseTree);
    }
}
