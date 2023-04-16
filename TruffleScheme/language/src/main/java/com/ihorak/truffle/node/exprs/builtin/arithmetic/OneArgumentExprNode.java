package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.PrimitiveProcedure;
import com.ihorak.truffle.type.SchemeBigInt;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemePair;
import com.ihorak.truffle.type.SchemeSymbol;
import com.ihorak.truffle.type.UndefinedValue;
import com.ihorak.truffle.type.UserDefinedProcedure;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.strings.TruffleString;


@NodeChild(value = "value")
public abstract class OneArgumentExprNode extends SchemeExpression {

    @Specialization
    protected long doLong(long value) {
        return value;
    }

    @Specialization
    protected SchemeBigInt doBigInt(SchemeBigInt value) {
        return value;
    }

    @Specialization
    protected double doDouble(double value) {
        return value;
    }

    @Specialization
    protected boolean doBoolean(boolean bool) {
        return bool;
    }

    @Specialization
    protected TruffleString doTruffleString(TruffleString truffleString) {
        return truffleString;
    }

    @Specialization
    protected SchemeList doList(SchemeList list) {
        return list;
    }

    @Specialization
    protected SchemePair doPair(SchemePair pair) {
        return pair;
    }

    @Specialization
    protected SchemeSymbol doSchemeSymbol(SchemeSymbol symbol) {
        return symbol;
    }

    @Specialization
    protected UserDefinedProcedure doUserDefineProcedure(UserDefinedProcedure procedure) {
        return procedure;
    }

    @Specialization
    protected PrimitiveProcedure doPrimitiveProcedure(PrimitiveProcedure procedure) {
        return procedure;
    }

    @Specialization
    protected UndefinedValue doUndefined(UndefinedValue undefinedValue) {
        return undefinedValue;
    }
}
