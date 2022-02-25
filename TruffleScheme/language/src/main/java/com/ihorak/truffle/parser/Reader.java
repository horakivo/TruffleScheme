package com.ihorak.truffle.parser;

import com.ihorak.truffle.context.Context;
import com.ihorak.truffle.context.Mode;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.SchemeRootNode;
import com.ihorak.truffle.parser.antlr.AntlrToProgram;
import com.ihorak.truffle.parser.antlr.R5RSLexer;
import com.ihorak.truffle.parser.antlr.R5RSParser;
import com.ihorak.truffle.parser.antlr.model.Program;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.ArrayList;
import java.util.List;

public class Reader {


    public static SchemeExpression readExpr(CharStream charStream) {
        var program = parseProgram(charStream);
        var globalContext = new Context();
        return ListToExpressionConverter.convert(program.getInternalRepresentations().get(0), globalContext);
    }

    public static SchemeExpression test(CharStream charStream, Context context) {
        var program = parseProgram(charStream);
        return ListToExpressionConverter.convert(program.getInternalRepresentations().get(0), context);
    }


    public static SchemeExpression readRuntimeExpr(CharStream charStream) {
        var program = parseProgram(charStream);
        var globalContext = new Context();
        globalContext.setMode(Mode.RUN_TIME);
        return ListToExpressionConverter.convert(program.getInternalRepresentations().get(0), globalContext);
    }

    public static SchemeRootNode readProgram(CharStream charStream) {

        var program = parseProgram(charStream);
        var globalContext = new Context();
        List<SchemeExpression> expressionList = new ArrayList<>();
        for (Object obj : program.getInternalRepresentations()) {
            expressionList.add(ListToExpressionConverter.convert(obj, globalContext));
        }

        return new SchemeRootNode(null, globalContext.getFrameDescriptor(), expressionList);
    }

    public static List<SchemeExpression> readProgram2(CharStream charStream, Context globalContext) {

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
