package com.ihorak.truffle;

import com.ihorak.truffle.node.builtin.CurrentMillisBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.DisplayBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.IsNullBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.ModuloBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.NotBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.arithmetic.DivideBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.arithmetic.MinusBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.arithmetic.MultiplyBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.arithmetic.PlusBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.comparison.EqualBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.comparison.EqualNumbersBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.comparison.LessThanBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.comparison.LessThanEqualBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.comparison.MoreThanBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.comparison.MoreThanEqualBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.list.AppendBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.list.CarBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.list.CdrBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.list.ConsBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.list.LengthBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.list.ListBuiltinNodeFactory;
import com.ihorak.truffle.runtime.PrimitiveProcedure;
import com.ihorak.truffle.runtime.SchemeSymbol;

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
        var appendPrimitiveProcedure = new PrimitiveProcedure("append", AppendBuiltinNodeFactory.getInstance());

        var equalPrimitiveProcedure = new PrimitiveProcedure("equal?", EqualBuiltinNodeFactory.getInstance());
        var equalNumbersPrimitiveProcedure = new PrimitiveProcedure("=", EqualNumbersBuiltinNodeFactory.getInstance());
        var moreThenPrimitiveProcedure = new PrimitiveProcedure(">", MoreThanBuiltinNodeFactory.getInstance());
        var moreThenEqualPrimitiveProcedure = new PrimitiveProcedure(">=", MoreThanEqualBuiltinNodeFactory.getInstance());
        var lessThenEqualPrimitiveProcedure = new PrimitiveProcedure("<=", LessThanEqualBuiltinNodeFactory.getInstance());
        var lessThenPrimitiveProcedure = new PrimitiveProcedure("<", LessThanBuiltinNodeFactory.getInstance());

        var moduloPrimitiveProcedure = new PrimitiveProcedure("modulo", ModuloBuiltinNodeFactory.getInstance());
        var isNullPrimitiveProcedure = new PrimitiveProcedure("null?", IsNullBuiltinNodeFactory.getInstance());
        var notPrimitiveProcedure = new PrimitiveProcedure("not", NotBuiltinNodeFactory.getInstance());
        var displayPrimitiveProcedure = new PrimitiveProcedure("display", DisplayBuiltinNodeFactory.getInstance());
        var currentMillisPrimitiveProcedure = new PrimitiveProcedure("current-milliseconds", CurrentMillisBuiltinNodeFactory.getInstance());

        var carPrimitiveProcedure = new PrimitiveProcedure("car", CarBuiltinNodeFactory.getInstance());
        var consPrimitiveProcedure = new PrimitiveProcedure("cons", ConsBuiltinNodeFactory.getInstance());
        var lengthPrimitiveProcedure = new PrimitiveProcedure("length", LengthBuiltinNodeFactory.getInstance());
        var cdrPrimitiveProcedure = new PrimitiveProcedure("cdr", CdrBuiltinNodeFactory.getInstance());


        result.put(new SchemeSymbol("+"), plusPrimitiveProcedure);
        result.put(new SchemeSymbol("-"), minusPrimitiveProcedure);
        result.put(new SchemeSymbol("*"), multiplyPrimitiveProcedure);
        result.put(new SchemeSymbol("/"), dividePrimaryProcedure);
        result.put(new SchemeSymbol("car"), carPrimitiveProcedure);
        result.put(new SchemeSymbol("cons"), consPrimitiveProcedure);
        result.put(new SchemeSymbol("length"), lengthPrimitiveProcedure);
        result.put(new SchemeSymbol("list"), listPrimitiveProcedure);
        result.put(new SchemeSymbol("cdr"), cdrPrimitiveProcedure);
        result.put(new SchemeSymbol("equal?"), equalPrimitiveProcedure);
        result.put(new SchemeSymbol("="), equalNumbersPrimitiveProcedure);
        result.put(new SchemeSymbol(">"), moreThenPrimitiveProcedure);
        result.put(new SchemeSymbol(">="), moreThenEqualPrimitiveProcedure);
        result.put(new SchemeSymbol("<"), lessThenPrimitiveProcedure);
        result.put(new SchemeSymbol("<="), lessThenEqualPrimitiveProcedure);
        result.put(new SchemeSymbol("modulo"), moduloPrimitiveProcedure);
        result.put(new SchemeSymbol("null?"), isNullPrimitiveProcedure);
        result.put(new SchemeSymbol("not"), notPrimitiveProcedure);
        result.put(new SchemeSymbol("display"), displayPrimitiveProcedure);
        result.put(new SchemeSymbol("current-milliseconds"), currentMillisPrimitiveProcedure);


        // result.put(new SchemeSymbol("map"), mapPrimitiveProcedure);
        result.put(new SchemeSymbol("append"), appendPrimitiveProcedure);

        return result;
    }
}
