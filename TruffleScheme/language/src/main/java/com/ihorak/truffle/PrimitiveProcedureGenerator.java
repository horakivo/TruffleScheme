package com.ihorak.truffle;

import com.ihorak.truffle.node.exprs.bbuiltin.arithmetic.DivideBuiltinNodeFactory;
import com.ihorak.truffle.node.exprs.bbuiltin.arithmetic.MinusBuiltinNodeFactory;
import com.ihorak.truffle.node.exprs.bbuiltin.arithmetic.MultiplyBuiltinNodeFactory;
import com.ihorak.truffle.node.exprs.bbuiltin.arithmetic.PlusBuiltinNodeFactory;
import com.ihorak.truffle.node.exprs.bbuiltin.comparison.LessThenBuiltinNodeFactory;
import com.ihorak.truffle.node.exprs.bbuiltin.list.CarBuiltinNodeFactory;
import com.ihorak.truffle.node.exprs.bbuiltin.list.CdrBuiltinNodeFactory;
import com.ihorak.truffle.node.exprs.bbuiltin.list.ConsBuiltinNode;
import com.ihorak.truffle.node.exprs.bbuiltin.list.ConsBuiltinNodeFactory;
import com.ihorak.truffle.node.exprs.bbuiltin.list.LengthBuiltinNode;
import com.ihorak.truffle.node.exprs.bbuiltin.list.LengthBuiltinNodeFactory;
import com.ihorak.truffle.node.exprs.bbuiltin.list.ListBuiltinNodeFactory;
import com.ihorak.truffle.type.PrimitiveProcedure;
import com.ihorak.truffle.type.SchemeSymbol;

import java.util.HashMap;
import java.util.Map;

public class PrimitiveProcedureGenerator {


    public static Map<SchemeSymbol, Object> generate() {
        HashMap<SchemeSymbol, Object> result = new HashMap<>();


        var plusPrimitiveProcedure = new PrimitiveProcedure("+", PlusBuiltinNodeFactory.getInstance());
        var minusPrimitiveProcedure = new PrimitiveProcedure("-", MinusBuiltinNodeFactory.getInstance());
        var multiplyPrimitiveProcedure = new PrimitiveProcedure("*", MultiplyBuiltinNodeFactory.getInstance());
        var dividePrimaryProcedure = new PrimitiveProcedure("/", DivideBuiltinNodeFactory.getInstance());


        var listPrimitiveProcedure = new PrimitiveProcedure("list", ListBuiltinNodeFactory.getInstance());
        //var mapPrimitiveProcedure = createArbitraryPrimitiveProcedure(MapExprNodeFactory.getInstance(), language, "map");
        //var appendPrimitiveProcedure = createArbitraryPrimitiveProcedure(AppendExprNodeFactory.getInstance(), language, "append");


//        var equalPrimitiveProcedure = createArbitraryPrimitiveProcedure(EqualPrimitiveProcedureNodeFactory.getInstance(), language, "=");
//        var moreThenPrimitiveProcedure = createArbitraryPrimitiveProcedure(MoreThenPrimitiveProcedureNodeFactory.getInstance(), language, ">");
//        var moreThenEqualPrimitiveProcedure = createArbitraryPrimitiveProcedure(MoreThenEqualPrimitiveProcedureNodeFactory.getInstance(), language, ">=");
//        var lessThenEqualPrimitiveProcedure = createArbitraryPrimitiveProcedure(LessThenEqualPrimitiveProcedureNodeFactory.getInstance(), language, "<=");
        var lessThenPrimitiveProcedure = new PrimitiveProcedure("<", LessThenBuiltinNodeFactory.getInstance());
//
        var carPrimitiveProcedure = new PrimitiveProcedure("car", CarBuiltinNodeFactory.getInstance());
        var consPrimitiveProcedure = new PrimitiveProcedure("cons", ConsBuiltinNodeFactory.getInstance());
        var lengthPrimitiveProcedure = new PrimitiveProcedure("length", LengthBuiltinNodeFactory.getInstance());
        var cdrPrimitiveProcedure = new PrimitiveProcedure("cdr", CdrBuiltinNodeFactory.getInstance());


        result.put(new SchemeSymbol("+"), plusPrimitiveProcedure);
        result.put(new SchemeSymbol("-"), minusPrimitiveProcedure);
        result.put(new SchemeSymbol("*"), multiplyPrimitiveProcedure);
        result.put(new SchemeSymbol("/"), dividePrimaryProcedure);
//


        result.put(new SchemeSymbol("car"), carPrimitiveProcedure);
        result.put(new SchemeSymbol("cons"), consPrimitiveProcedure);
        result.put(new SchemeSymbol("length"), lengthPrimitiveProcedure);
        result.put(new SchemeSymbol("list"), listPrimitiveProcedure);
        result.put(new SchemeSymbol("cdr"), cdrPrimitiveProcedure);
//        result.put(new SchemeSymbol("="), equalPrimitiveProcedure);
//        result.put(new SchemeSymbol(">"), moreThenPrimitiveProcedure);
//        result.put(new SchemeSymbol(">="), moreThenEqualPrimitiveProcedure);
        result.put(new SchemeSymbol("<"), lessThenPrimitiveProcedure);
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
