package com.ihorak.truffle;

import com.ihorak.truffle.node.callable.ProcedureRootNode;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.BuiltinExpression;
import com.ihorak.truffle.node.exprs.PrimitiveProcedureRootNode;
import com.ihorak.truffle.node.exprs.arithmetic.*;
import com.ihorak.truffle.node.exprs.shared.CarExprNodeFactory;
import com.ihorak.truffle.node.exprs.shared.ConsExprNodeFactory;
import com.ihorak.truffle.node.scope.ReadProcedureArgExprNode;
import com.ihorak.truffle.node.exprs.builtin.EvalExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.arithmetic.*;
import com.ihorak.truffle.type.PrimitiveProcedure;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.frame.FrameDescriptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class PrimitiveProcedureGenerator {


    public static Map<SchemeSymbol, Object> generate(SchemeTruffleLanguage language) {
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
        var carPrimitiveProcedure = createPrimitiveProcedure(CarExprNodeFactory.getInstance(), language, "car");
        var consPrimitiveProcedure = createPrimitiveProcedure(ConsExprNodeFactory.getInstance(), language, "cons");


        return new HashMap<>(Map.of(
                new SchemeSymbol("+"), plusFunction,
                new SchemeSymbol("-"), minusFunction,
                new SchemeSymbol("*"), multiplyFunction,
                new SchemeSymbol("/"), divideFunction,
                new SchemeSymbol("eval"), evalFunction,
                new SchemeSymbol("car"), carPrimitiveProcedure,
                new SchemeSymbol("cons"), consPrimitiveProcedure
        ));
    }

    public static PrimitiveProcedure createPrimitiveProcedure(SchemeExpression schemeExpression, Integer expectedNumberOfArgs, SchemeTruffleLanguage language) {
        var rootNode = new ProcedureRootNode(language, new FrameDescriptor(), List.of(schemeExpression));

        return new PrimitiveProcedure(rootNode.getCallTarget(), expectedNumberOfArgs, "not impl yet");
    }

    public static PrimitiveProcedure createPrimitiveProcedure(NodeFactory<? extends BuiltinExpression> factory, SchemeTruffleLanguage language, String name) {
        var expectedNumberOfArgs = factory.getExecutionSignature().size();
        ReadProcedureArgExprNode[] arguments =
                IntStream
                        .range(0, expectedNumberOfArgs)
                        .mapToObj(ReadProcedureArgExprNode::new)
                        .toArray(ReadProcedureArgExprNode[]::new);

        var expression = factory.createNode((Object) arguments);
        var rootNode = new PrimitiveProcedureRootNode(language, expression);

        return new PrimitiveProcedure(rootNode.getCallTarget(), expectedNumberOfArgs, name);


    }
}
