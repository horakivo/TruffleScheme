package com.ihorak.truffle.node.exprs.core.comperison;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.exprs.core.BinaryOperationNode;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

import java.math.BigInteger;

public abstract class MoreThenEqualBinaryNode extends BinaryOperationNode {

    @Specialization
    protected boolean moreThenEqualLongs(long left, long right) {
        return left >= right;
    }

    /*
     * 0: if the value of this BigInteger is equal to that of the BigInteger object passed as a parameter.
     * 1: if the value of this BigInteger is greater than that of the BigInteger object passed as a parameter.
     * -1: if the value of this BigInteger is less than that of the BigInteger object passed as a parameter/
     */
    @Specialization
    protected boolean moreThenEqualBigInts(BigInteger left, BigInteger right) {
        var result = left.compareTo(right);
        return result >= 0;
    }

    @TruffleBoundary
    @Fallback
    protected Object fallback(Object left, Object right) {
        throw new SchemeException(">=: contract violation\nexpected: real?\ngiven left: " + left + "\ngiven right: " + right, this);
    }
}
