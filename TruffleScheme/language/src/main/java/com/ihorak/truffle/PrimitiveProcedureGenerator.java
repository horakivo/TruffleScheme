package com.ihorak.truffle;

import com.ihorak.truffle.node.callable.ProcedureRootNode;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.PrimitiveProcedureRootNode;
import com.ihorak.truffle.node.exprs.ReadProcedureArgsExprNodeGen;
import com.ihorak.truffle.node.exprs.primitive_procedure.ListPrimitiveProcedureNodeFactory;
import com.ihorak.truffle.node.exprs.primitive_procedure.arithmetic.DividePrimitiveProcedureNodeFactory;
import com.ihorak.truffle.node.exprs.primitive_procedure.arithmetic.MinusPrimitiveProcedureNodeFactory;
import com.ihorak.truffle.node.exprs.primitive_procedure.arithmetic.MultiplyPrimitiveProcedureNodeFactory;
import com.ihorak.truffle.node.exprs.primitive_procedure.arithmetic.PlusPrimitiveProcedureNodeFactory;
import com.ihorak.truffle.node.exprs.primitive_procedure.comparison.*;
import com.ihorak.truffle.node.exprs.shared.CarExprNodeFactory;
import com.ihorak.truffle.node.exprs.shared.CdrExprNodeFactory;
import com.ihorak.truffle.node.exprs.shared.ConsExprNodeFactory;
import com.ihorak.truffle.node.exprs.shared.LengthExprNodeFactory;
import com.ihorak.truffle.node.scope.ReadProcedureArgExprNode;
import com.ihorak.truffle.node.exprs.builtin.EvalExprNodeGen;
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
        HashMap<SchemeSymbol, Object> result = new HashMap<>();
        var plusPrimitiveProcedure = createArbitraryPrimitiveProcedure(PlusPrimitiveProcedureNodeFactory.getInstance(), language, "+");
        var minusPrimitiveProcedure = createArbitraryPrimitiveProcedure(MinusPrimitiveProcedureNodeFactory.getInstance(), language, "-");
        var multiplyPrimitiveProcedure = createArbitraryPrimitiveProcedure(MultiplyPrimitiveProcedureNodeFactory.getInstance(), language, "*");
        var dividePrimaryProcedure = createArbitraryPrimitiveProcedure(DividePrimitiveProcedureNodeFactory.getInstance(), language, "/");
        var listPrimitiveProcedure = createArbitraryPrimitiveProcedure(ListPrimitiveProcedureNodeFactory.getInstance(), language, "list");

        var equalPrimitiveProcedure = createArbitraryPrimitiveProcedure(EqualPrimitiveProcedureNodeFactory.getInstance(), language, "=");
        var moreThenPrimitiveProcedure = createArbitraryPrimitiveProcedure(MoreThenPrimitiveProcedureNodeFactory.getInstance(), language, ">");
        var moreThenEqualPrimitiveProcedure = createArbitraryPrimitiveProcedure(MoreThenEqualPrimitiveProcedureNodeFactory.getInstance(), language, ">=");
        var lessThenEqualPrimitiveProcedure = createArbitraryPrimitiveProcedure(LessThenEqualPrimitiveProcedureNodeFactory.getInstance(), language, "<=");
        var lessThenPrimitiveProcedure = createArbitraryPrimitiveProcedure(LessThenPrimitiveProcedureNodeFactory.getInstance(), language, "<");

        var evalExpr = EvalExprNodeGen.create(new ReadProcedureArgExprNode(0));
        var evalProcedure = createPrimitiveProcedure(evalExpr, 1, language);


        var carPrimitiveProcedure = createPrimitiveProcedure(CarExprNodeFactory.getInstance(), language, "car");
        var consPrimitiveProcedure = createPrimitiveProcedure(ConsExprNodeFactory.getInstance(), language, "cons");
        var lengthPrimitiveProcedure = createPrimitiveProcedure(LengthExprNodeFactory.getInstance(), language, "length");
        var cdrPrimitiveProcedure = createPrimitiveProcedure(CdrExprNodeFactory.getInstance(), language, "cdr");


        result.put(new SchemeSymbol("+"), plusPrimitiveProcedure);
        result.put(new SchemeSymbol("-"), minusPrimitiveProcedure);
        result.put(new SchemeSymbol("*"), multiplyPrimitiveProcedure);
        result.put(new SchemeSymbol("/"), dividePrimaryProcedure);
        result.put(new SchemeSymbol("eval"), evalProcedure);
        result.put(new SchemeSymbol("car"), carPrimitiveProcedure);
        result.put(new SchemeSymbol("cons"), consPrimitiveProcedure);
        result.put(new SchemeSymbol("length"), lengthPrimitiveProcedure);
        result.put(new SchemeSymbol("list"), listPrimitiveProcedure);
        result.put(new SchemeSymbol("cdr"), cdrPrimitiveProcedure);
        result.put(new SchemeSymbol("="), equalPrimitiveProcedure);
        result.put(new SchemeSymbol(">"), moreThenPrimitiveProcedure);
        result.put(new SchemeSymbol(">="), moreThenEqualPrimitiveProcedure);
        result.put(new SchemeSymbol("<"), lessThenPrimitiveProcedure);
        result.put(new SchemeSymbol("<="), lessThenEqualPrimitiveProcedure);

        return result;
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

        var expression = factory.createNode(ReadProcedureArgsExprNodeGen.create());
        var rootNode = new PrimitiveProcedureRootNode(language, expression);

        return new PrimitiveProcedure(rootNode.getCallTarget(), null, name);
    }
}
