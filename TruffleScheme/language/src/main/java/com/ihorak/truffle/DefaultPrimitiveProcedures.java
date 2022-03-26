package com.ihorak.truffle;

import com.ihorak.truffle.node.callable.ProcedureRootNode;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.scope.ReadProcedureArgExprNode;
import com.ihorak.truffle.node.exprs.arithmetic.ReduceDivideExprRuntimeNodeGen;
import com.ihorak.truffle.node.exprs.arithmetic.ReduceMinusExprRuntimeNodeGen;
import com.ihorak.truffle.node.exprs.arithmetic.ReduceMultiplyExprRuntimeNodeGen;
import com.ihorak.truffle.node.exprs.arithmetic.ReducePlusExprRuntimeNodeGen;
import com.ihorak.truffle.node.exprs.builtin.EvalExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.arithmetic.*;
import com.ihorak.truffle.node.scope.WritePrimitiveProcedureExprNode;
import com.ihorak.truffle.node.scope.WritePrimitiveProcedureExprNodeGen;
import com.ihorak.truffle.type.PrimitiveProcedure;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.frame.FrameDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultPrimitiveProcedures {


    public static List<WritePrimitiveProcedureExprNode> generate(SchemeTruffleLanguage language) {
        List<WritePrimitiveProcedureExprNode> result = new ArrayList<>();

        var builtinFunctionsMap = getAllPrimitiveProcedures(language);

        for (SchemeSymbol symbol : builtinFunctionsMap.keySet()) {
            result.add(WritePrimitiveProcedureExprNodeGen.create(builtinFunctionsMap.get(symbol), symbol));
        }

        return result;
    }

    private static Map<SchemeSymbol, PrimitiveProcedure> getAllPrimitiveProcedures(SchemeTruffleLanguage language) {
//        var plusExpr = PlusTestNodeGen.create(new ReadProcedureArgExprNode(0), new ReadProcedureArgExprNode(1));
        var plusExpr = ReducePlusExprRuntimeNodeGen.create(PlusExprNodeGen.create());
        var plusFunction = createPrimitiveProcedure(plusExpr, null, language);
        var minusExpr = ReduceMinusExprRuntimeNodeGen.create(MinusExprNodeGen.create());
        var minusFunction = createPrimitiveProcedure(minusExpr, null, language);
        var multiplyExpr = ReduceMultiplyExprRuntimeNodeGen.create(MultiplyExprNodeGen.create());
        var multiplyFunction = createPrimitiveProcedure(multiplyExpr, null, language);
        var divideExpr = ReduceDivideExprRuntimeNodeGen.create(DivideExprNodeGen.create());
        var divideFunction = createPrimitiveProcedure(divideExpr, null, language);
        var evalExpr = EvalExprNodeGen.create(new ReadProcedureArgExprNode(0));
        var evalFunction = createPrimitiveProcedure(evalExpr, 1, language);


        return Map.of(
                new SchemeSymbol("+"), plusFunction,
                new SchemeSymbol("-"), minusFunction,
                new SchemeSymbol("*"), multiplyFunction,
                new SchemeSymbol("/"), divideFunction
        );
    }

    public static PrimitiveProcedure createPrimitiveProcedure(SchemeExpression schemeExpression, Integer expectedNumberOfArgs, SchemeTruffleLanguage language) {
        var rootNode = new ProcedureRootNode(language, new FrameDescriptor(), List.of(schemeExpression));

        return new PrimitiveProcedure(rootNode.getCallTarget(), expectedNumberOfArgs);
    }
}
