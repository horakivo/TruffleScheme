package com.ihorak.truffle.convertor.special_form;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.runtime.PrimitiveProcedure;
import com.ihorak.truffle.runtime.SchemeBigInt;
import com.ihorak.truffle.runtime.SchemeList;
import com.ihorak.truffle.runtime.SchemePair;
import com.ihorak.truffle.runtime.SchemeSymbol;
import com.ihorak.truffle.runtime.UndefinedValue;
import com.ihorak.truffle.runtime.UserDefinedProcedure;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.strings.TruffleString;


@NodeChild(value = "value")
public abstract class EvalToSelfExprNode extends SchemeExpression {

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
