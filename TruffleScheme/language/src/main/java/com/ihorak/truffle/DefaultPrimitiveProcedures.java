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
import com.ihorak.truffle.node.scope.WriteBuiltinProcedureExprNode;
import com.ihorak.truffle.node.scope.WriteBuiltinProcedureExprNodeGen;
import com.ihorak.truffle.type.SchemeFunction;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.frame.FrameDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultPrimitiveProcedures {


    public static List<WriteBuiltinProcedureExprNode> generate() {
        List<WriteBuiltinProcedureExprNode> writeBuiltinProcedureExprNodes = new ArrayList<>();

        var builtinFunctionsMap = getAllPrimitiveProcedures();

        for (SchemeSymbol symbol : builtinFunctionsMap.keySet()) {
            writeBuiltinProcedureExprNodes.add(WriteBuiltinProcedureExprNodeGen.create(builtinFunctionsMap.get(symbol), symbol));
        }

        return writeBuiltinProcedureExprNodes;
    }

    private static Map<SchemeSymbol, SchemeFunction> getAllPrimitiveProcedures() {
//        var plusExpr = PlusTestNodeGen.create(new ReadProcedureArgExprNode(0), new ReadProcedureArgExprNode(1));
        var plusExpr = ReducePlusExprRuntimeNodeGen.create(PlusExprNodeGen.create());
        SchemeFunction plusFunction = createBuiltinFunction(plusExpr, null);
        var minusExpr = ReduceMinusExprRuntimeNodeGen.create(MinusExprNodeGen.create());
        SchemeFunction minusFunction = createBuiltinFunction(minusExpr, null);
        var multiplyExpr = ReduceMultiplyExprRuntimeNodeGen.create(MultiplyExprNodeGen.create());
        SchemeFunction multiplyFunction = createBuiltinFunction(multiplyExpr, null);
        var divideExpr = ReduceDivideExprRuntimeNodeGen.create(DivideExprNodeGen.create());
        SchemeFunction divideFunction = createBuiltinFunction(divideExpr, null);
        SchemeExpression evalExpr = EvalExprNodeGen.create(new ReadProcedureArgExprNode(0));
        SchemeFunction evalFunction = createBuiltinFunction(evalExpr, 1);


        return Map.of(
                new SchemeSymbol("+"), plusFunction,
                new SchemeSymbol("-"), minusFunction,
                new SchemeSymbol("*"), multiplyFunction,
                new SchemeSymbol("/"), divideFunction
        );
    }

    public static SchemeFunction createBuiltinFunction(SchemeExpression schemeExpression, Integer expectedNumberOfArgs) {
        var rootNode = new ProcedureRootNode(null, new FrameDescriptor(), List.of(schemeExpression));

        return new SchemeFunction(rootNode.getCallTarget(), expectedNumberOfArgs, false);
    }
}
