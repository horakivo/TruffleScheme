package com.ihorak.truffle;

import com.ihorak.truffle.node.exprs.*;
import com.ihorak.truffle.node.exprs.primitive_procedure.arithmetic.DividePrimitiveProcedureNodeFactory;
import com.ihorak.truffle.node.exprs.primitive_procedure.arithmetic.MinusPrimitiveProcedureNodeFactory;
import com.ihorak.truffle.node.exprs.primitive_procedure.arithmetic.MultiplyPrimitiveProcedureNodeFactory;
import com.ihorak.truffle.node.exprs.primitive_procedure.arithmetic.PlusPrimitiveProcedureNodeFactory;
import com.ihorak.truffle.node.exprs.primitive_procedure.comparison.*;
import com.ihorak.truffle.node.exprs.shared.*;
import com.ihorak.truffle.node.scope.ReadLocalProcedureArgExprNode;
import com.ihorak.truffle.type.PrimitiveProcedure;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.dsl.NodeFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class PrimitiveProcedureGenerator {


    public static Map<SchemeSymbol, Object> generate(SchemeTruffleLanguage language) {
        HashMap<SchemeSymbol, Object> result = new HashMap<>();
        var plusPrimitiveProcedure = createBinaryReduciblePrimitiveProcedure(PlusPrimitiveProcedureNodeFactory.getInstance(), language, "+");
        var minusPrimitiveProcedure = createBinaryReduciblePrimitiveProcedure(MinusPrimitiveProcedureNodeFactory.getInstance(), language, "-");
        var multiplyPrimitiveProcedure = createBinaryReduciblePrimitiveProcedure(MultiplyPrimitiveProcedureNodeFactory.getInstance(), language, "*");
        var dividePrimaryProcedure = createBinaryReduciblePrimitiveProcedure(DividePrimitiveProcedureNodeFactory.getInstance(), language, "/");

        var listPrimitiveProcedure = createArbitraryPrimitiveProcedure(ListExprNodeFactory.getInstance(), language, "list");
        //var mapPrimitiveProcedure = createArbitraryPrimitiveProcedure(MapExprNodeFactory.getInstance(), language, "map");
        //var appendPrimitiveProcedure = createArbitraryPrimitiveProcedure(AppendExprNodeFactory.getInstance(), language, "append");

        var equalPrimitiveProcedure = createBinaryReduciblePrimitiveProcedure(EqualPrimitiveProcedureNodeFactory.getInstance(), language, "=");
        var moreThenPrimitiveProcedure = createBinaryReduciblePrimitiveProcedure(MoreThenPrimitiveProcedureNodeFactory.getInstance(), language, ">");
        var moreThenEqualPrimitiveProcedure = createBinaryReduciblePrimitiveProcedure(MoreThenEqualPrimitiveProcedureNodeFactory.getInstance(), language, ">=");
        var lessThenEqualPrimitiveProcedure = createBinaryReduciblePrimitiveProcedure(LessThenEqualPrimitiveProcedureNodeFactory.getInstance(), language, "<=");
        var lessThenPrimitiveProcedure = createBinaryReduciblePrimitiveProcedure(LessThenPrimitiveProcedureNodeFactory.getInstance(), language, "<");

        var evalPrimitiveProcedure = createLimitedPrimitiveProcedure(EvalExprNodeFactory.getInstance(), language, "eval");
        var carPrimitiveProcedure = createLimitedPrimitiveProcedure(CarExprNodeFactory.getInstance(), language, "car");
        var consPrimitiveProcedure = createLimitedPrimitiveProcedure(ConsExprNodeFactory.getInstance(), language, "cons");
        var lengthPrimitiveProcedure = createLimitedPrimitiveProcedure(LengthExprNodeFactory.getInstance(), language, "length");
        var cdrPrimitiveProcedure = createLimitedPrimitiveProcedure(CdrExprNodeFactory.getInstance(), language, "cdr");


        result.put(new SchemeSymbol("+"), plusPrimitiveProcedure);
        result.put(new SchemeSymbol("-"), minusPrimitiveProcedure);
        result.put(new SchemeSymbol("*"), multiplyPrimitiveProcedure);
        result.put(new SchemeSymbol("/"), dividePrimaryProcedure);
        result.put(new SchemeSymbol("eval"), evalPrimitiveProcedure);
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
       // result.put(new SchemeSymbol("map"), mapPrimitiveProcedure);
       //result.put(new SchemeSymbol("append"), appendPrimitiveProcedure);

        return result;
    }

    public static PrimitiveProcedure createArbitraryPrimitiveProcedure(NodeFactory<? extends ArbitraryBuiltin> factory, SchemeTruffleLanguage language, String name) {
        var arbitraryBuiltinExpr = factory.createNode(ReadProcedureArgsExprNodeGen.create());
        var rootNode = new PrimitiveProcedureRootNode(language, arbitraryBuiltinExpr);


        return new PrimitiveProcedure(rootNode.getCallTarget(), null, name);
    }

    public static PrimitiveProcedure createLimitedPrimitiveProcedure(NodeFactory<? extends LimitedBuiltin> factory, SchemeTruffleLanguage language, String name) {
        var expectedNumberOfArgs = factory.getExecutionSignature().size();
        ReadLocalProcedureArgExprNode[] arguments =
                IntStream
                        .range(0, expectedNumberOfArgs)
                        .mapToObj(ReadLocalProcedureArgExprNode::new)
                        .toArray(ReadLocalProcedureArgExprNode[]::new);

        var expression = factory.createNode((Object) arguments);
        var rootNode = new PrimitiveProcedureRootNode(language, expression);

        return new PrimitiveProcedure(rootNode.getCallTarget(), expectedNumberOfArgs, name);
    }

    public static PrimitiveProcedure createBinaryReduciblePrimitiveProcedure(NodeFactory<? extends BinaryReducibleBuiltin> factory, SchemeTruffleLanguage language, String name) {

        var expression = factory.createNode(ReadProcedureArgsExprNodeGen.create());
        var rootNode = new PrimitiveProcedureRootNode(language, expression);

        return new PrimitiveProcedure(rootNode.getCallTarget(), null, name);
    }
}
