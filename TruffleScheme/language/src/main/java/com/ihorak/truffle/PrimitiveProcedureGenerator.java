package com.ihorak.truffle;

import com.ihorak.truffle.node.callable.AlwaysInlinedMethodNode;
import com.ihorak.truffle.node.exprs.primitive_procedure.arithmetic.PlusPrimitiveProcedureNodeFactory;
import com.ihorak.truffle.type.ArbitraryArgsPrimitiveProcedure;
import com.ihorak.truffle.type.FixedArgsPrimitiveProcedure;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.dsl.NodeFactory;

import java.util.HashMap;
import java.util.Map;

public class PrimitiveProcedureGenerator {


    public static Map<SchemeSymbol, Object> generate(SchemeTruffleLanguage language) {
        HashMap<SchemeSymbol, Object> result = new HashMap<>();


        var plusPrimitiveProcedure = new ArbitraryArgsPrimitiveProcedure("+", PlusPrimitiveProcedureNodeFactory.getInstance());
//        var minusPrimitiveProcedure = createArbitraryPrimitiveProcedure(MinusPrimitiveProcedureNodeFactory.getInstance(), language, "-");
//        var multiplyPrimitiveProcedure = createArbitraryPrimitiveProcedure(MultiplyPrimitiveProcedureNodeFactory.getInstance(), language, "*");
//        var dividePrimaryProcedure = createArbitraryPrimitiveProcedure(DividePrimitiveProcedureNodeFactory.getInstance(), language, "/");


        //var listPrimitiveProcedure = createArbitraryPrimitiveProcedure(ListExprNodeFactory.getInstance(), language, "list");
        //var mapPrimitiveProcedure = createArbitraryPrimitiveProcedure(MapExprNodeFactory.getInstance(), language, "map");
        //var appendPrimitiveProcedure = createArbitraryPrimitiveProcedure(AppendExprNodeFactory.getInstance(), language, "append");


//        var equalPrimitiveProcedure = createArbitraryPrimitiveProcedure(EqualPrimitiveProcedureNodeFactory.getInstance(), language, "=");
//        var moreThenPrimitiveProcedure = createArbitraryPrimitiveProcedure(MoreThenPrimitiveProcedureNodeFactory.getInstance(), language, ">");
//        var moreThenEqualPrimitiveProcedure = createArbitraryPrimitiveProcedure(MoreThenEqualPrimitiveProcedureNodeFactory.getInstance(), language, ">=");
//        var lessThenEqualPrimitiveProcedure = createArbitraryPrimitiveProcedure(LessThenEqualPrimitiveProcedureNodeFactory.getInstance(), language, "<=");
//        var lessThenPrimitiveProcedure = createArbitraryPrimitiveProcedure(LessThenPrimitiveProcedureNodeFactory.getInstance(), language, "<");
//
        //var carPrimitiveProcedure = new FixedArgsPrimitiveProcedure("car", )
//        var consPrimitiveProcedure = createLimitedPrimitiveProcedure(ConsExprNodeFactory.getInstance(), language, "cons");
//        var lengthPrimitiveProcedure = createLimitedPrimitiveProcedure(LengthExprNodeFactory.getInstance(), language, "length");
//        var cdrPrimitiveProcedure = createLimitedPrimitiveProcedure(CdrExprNodeFactory.getInstance(), language, "cdr");


        result.put(new SchemeSymbol("+"), plusPrimitiveProcedure);
//        result.put(new SchemeSymbol("-"), minusPrimitiveProcedure);
//        result.put(new SchemeSymbol("*"), multiplyPrimitiveProcedure);
//        result.put(new SchemeSymbol("/"), dividePrimaryProcedure);
//


        //result.put(new SchemeSymbol("car"), carPrimitiveProcedure);
//        result.put(new SchemeSymbol("cons"), consPrimitiveProcedure);
//        result.put(new SchemeSymbol("length"), lengthPrimitiveProcedure);
//        //result.put(new SchemeSymbol("list"), listPrimitiveProcedure);
//        result.put(new SchemeSymbol("cdr"), cdrPrimitiveProcedure);
//        result.put(new SchemeSymbol("="), equalPrimitiveProcedure);
//        result.put(new SchemeSymbol(">"), moreThenPrimitiveProcedure);
//        result.put(new SchemeSymbol(">="), moreThenEqualPrimitiveProcedure);
//        result.put(new SchemeSymbol("<"), lessThenPrimitiveProcedure);
//        result.put(new SchemeSymbol("<="), lessThenEqualPrimitiveProcedure);


        // result.put(new SchemeSymbol("map"), mapPrimitiveProcedure);
        //result.put(new SchemeSymbol("append"), appendPrimitiveProcedure);

        return result;
    }

//    public static ArbitraryArgsPrimitiveProcedure createArbitraryPrimitiveProcedure(NodeFactory<? extends AlwaysInlinedMethodNode> factory, SchemeTruffleLanguage language, String name) {
////        var arbitraryBuiltinExpr = factory.createNode(ReadProcedureArgsExprNodeGen.create());
////        var test = factory.getExecutionSignature().size();
////        var rootNode = new PrimitiveProcedureRootNode(language, arbitraryBuiltinExpr);
//
//
//        return new ArbitraryArgsPrimitiveProcedure(factory, name);
//    }

//    public static PrimitiveProcedure createArbitraryPrimitiveProcedure(NodeFactory<? extends ArbitraryNumberOfArgsBuiltin> factory, SchemeTruffleLanguage language, String name) {
//        var arbitraryBuiltinExpr = factory.createNode(ReadProcedureArgsExprNodeGen.create());
//        var test = factory.getExecutionSignature().size();
//        var rootNode = new PrimitiveProcedureRootNode(language, arbitraryBuiltinExpr);
//
//
//        return new PrimitiveProcedure(factory, name);
//    }
//
//    public static PrimitiveProcedure createLimitedPrimitiveProcedure(NodeFactory<? extends GivenNumberOfArgsBuiltin> factory, SchemeTruffleLanguage language, String name) {
//        var expectedNumberOfArgs = factory.getExecutionSignature().size();
//        ReadProcedureArgExprNode[] arguments =
//                IntStream
//                        .range(0, expectedNumberOfArgs)
//                        .mapToObj(ReadProcedureArgExprNode::new)
//                        .toArray(ReadProcedureArgExprNode[]::new);
//
//        var expression = factory.createNode((Object) arguments);
//        var rootNode = new PrimitiveProcedureRootNode(language, expression);
//
//        return new PrimitiveProcedure(factory, expectedNumberOfArgs, name);
//    }
}
