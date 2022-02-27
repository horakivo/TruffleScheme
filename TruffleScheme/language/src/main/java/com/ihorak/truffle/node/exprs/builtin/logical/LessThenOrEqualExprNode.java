package com.ihorak.truffle.node.exprs.builtin.logical;

import com.ihorak.truffle.SchemeException;
import com.ihorak.truffle.node.exprs.builtin.BinaryOperationNode;
import com.oracle.truffle.api.dsl.Specialization;

import java.math.BigInteger;

public abstract class LessThenOrEqualExprNode extends BinaryOperationNode {

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
