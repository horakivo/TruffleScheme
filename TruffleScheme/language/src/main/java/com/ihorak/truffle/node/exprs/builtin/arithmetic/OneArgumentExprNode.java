package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.UndefinedValue;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;

import java.math.BigInteger;

@NodeChild(value = "value")
public abstract class OneArgumentExprNode extends SchemeExpression {

    @Specialization
    protected long doLong(long value) {
        return value;
    }

    @Specialization
    protected BigInteger doBigInt(BigInteger value) {
        return value;
    }

    @Specialization
    protected UndefinedValue doUndefined(UndefinedValue undefinedValue) {
        return undefinedValue;
    }

    @Specialization
    protected double doDouble(double value) {
        return value;
    }
}
