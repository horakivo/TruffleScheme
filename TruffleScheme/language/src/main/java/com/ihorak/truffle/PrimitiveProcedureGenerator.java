package com.ihorak.truffle;

import com.ihorak.truffle.node.callable.ProcedureRootNode;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.BuiltinExpression;
import com.ihorak.truffle.node.exprs.PrimitiveProcedureRootNode;
import com.ihorak.truffle.node.exprs.ReadProceduresArgsExprNode;
import com.ihorak.truffle.node.exprs.ReadProceduresArgsExprNodeGen;
import com.ihorak.truffle.node.exprs.arithmetic.*;
import com.ihorak.truffle.node.exprs.primitive_procedure.ListPrimitiveProcedureNodeFactory;
import com.ihorak.truffle.node.exprs.primitive_procedure.MinusPrimitiveProcedureNodeFactory;
import com.ihorak.truffle.node.exprs.primitive_procedure.MultiplyPrimitiveProcedureNodeFactory;
import com.ihorak.truffle.node.exprs.primitive_procedure.PlusPrimitiveProcedureNodeFactory;
import com.ihorak.truffle.node.exprs.primitive_procedure.arithmetic.MinusBinaryNodeGen;
import com.ihorak.truffle.node.exprs.shared.CarExprNodeFactory;
import com.ihorak.truffle.node.exprs.shared.ConsExprNodeFactory;
import com.ihorak.truffle.node.exprs.shared.LengthExprNodeFactory;
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
        var plusPrimitiveProcedure = createArbitraryPrimitiveProcedure(PlusPrimitiveProcedureNodeFactory.getInstance(), language, "+");
        var minusPrimitiveProcedure = createArbitraryPrimitiveProcedure(MinusPrimitiveProcedureNodeFactory.getInstance(), language, "-");
        var multiplyPrimitiveProcedure = createArbitraryPrimitiveProcedure(MultiplyPrimitiveProcedureNodeFactory.getInstance(), language, "*");
        var divideExpr = ReduceDivideExprRuntimeNodeGen.create(DivideExprNodeGen.create());
        var divideFunction = createPrimitiveProcedure(divideExpr, null, language);
        var evalExpr = EvalExprNodeGen.create(new ReadProcedureArgExprNode(0));
        var evalFunction = createPrimitiveProcedure(evalExpr, 1, language);
        var carPrimitiveProcedure = createPrimitiveProcedure(CarExprNodeFactory.getInstance(), language, "car");
        var consPrimitiveProcedure = createPrimitiveProcedure(ConsExprNodeFactory.getInstance(), language, "cons");
        var lengthPrimitiveProcedure = createPrimitiveProcedure(LengthExprNodeFactory.getInstance(), language, "length");
        var listPrimitiveProcedure = createArbitraryPrimitiveProcedure(ListPrimitiveProcedureNodeFactory.getInstance(), language, "list");


        return new HashMap<>(Map.of(
                new SchemeSymbol("+"), plusPrimitiveProcedure,
                new SchemeSymbol("-"), minusPrimitiveProcedure,
                new SchemeSymbol("*"), multiplyPrimitiveProcedure,
                new SchemeSymbol("/"), divideFunction,
                new SchemeSymbol("eval"), evalFunction,
                new SchemeSymbol("car"), carPrimitiveProcedure,
                new SchemeSymbol("cons"), consPrimitiveProcedure,
                new SchemeSymbol("length"), lengthPrimitiveProcedure,
                new SchemeSymbol("list"), listPrimitiveProcedure
        ));
    }

    public static PrimitiveProcedure createPrimitiveProcedure(SchemeExpression schemeExpression, Integer expectedNumberOfArgs, SchemeTruffleLanguage language) {
        var rootNode = new ProcedureRootNode(language, new FrameDescriptor(), List.of(schemeExpression));

        return new PrimitiveProcedure(rootNode.getCallTarget(), expectedNumberOfArgs, "not impl yet");
    }

    public static PrimitiveProcedure createPrimitiveProcedure(NodeFactory<? extends SchemeExpression> factory, SchemeTruffleLanguage language, String name) {
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

    public static PrimitiveProcedure createArbitraryPrimitiveProcedure(NodeFactory<? extends SchemeExpression> factory, SchemeTruffleLanguage language, String name) {

        var expression = factory.createNode(ReadProceduresArgsExprNodeGen.create());
        var rootNode = new PrimitiveProcedureRootNode(language, expression);

        return new PrimitiveProcedure(rootNode.getCallTarget(), null, name);
    }
}
