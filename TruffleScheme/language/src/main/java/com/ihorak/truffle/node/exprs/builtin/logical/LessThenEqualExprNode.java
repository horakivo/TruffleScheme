package com.ihorak.truffle.node.exprs.builtin.logical;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;

import java.math.BigInteger;

@NodeChild(value = "left")
@NodeChild(value = "right")
public abstract class LessThenEqualExprNode extends SchemeExpression {

    @Specialization
    protected boolean lessThenEqualLongs(long left, long right) {
        return left <= right;
    }

    /*
     * 0: if the value of this BigInteger is equal to that of the BigInteger object passed as a parameter.
     * 1: if the value of this BigInteger is greater than that of the BigInteger object passed as a parameter.
     * -1: if the value of this BigInteger is less than that of the BigInteger object passed as a parameter/
     */
    @Specialization
    protected boolean lessThenEqualBigInts(BigInteger left, BigInteger right) {
        var result = left.compareTo(right);
        return result <= 0;
    }
}
