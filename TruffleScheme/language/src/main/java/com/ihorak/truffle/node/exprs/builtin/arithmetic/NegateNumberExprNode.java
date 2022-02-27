package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;

import java.math.BigInteger;

@NodeChild(value = "number")
public abstract class NegateNumberExprNode extends SchemeExpression {

    @Specialization
    protected long negateLong(long value) {
        return -value;
    }

    @Specialization
    protected BigInteger negateBigInt(BigInteger value) {
        return value.negate();
    }
}
