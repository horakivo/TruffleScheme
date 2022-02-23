package com.ihorak.truffle.parser.antlr;

import com.ihorak.truffle.parser.antlr.model.Program;

import java.util.ArrayList;
import java.util.List;

public class AntlrToProgram extends R5RSBaseVisitor<Program> {

    @Override
    public Program visitProgram(R5RSParser.ProgramContext ctx) {
        var antlrToExpression = new AntlrToInternalRepresentation();
        List<Object> resultList = new ArrayList<>();
        for (int i = 0; i < ctx.getChildCount() - 1; i++) {
            resultList.add(antlrToExpression.visit(ctx.getChild(i)));
        }

        return new Program(resultList);
    }
}
