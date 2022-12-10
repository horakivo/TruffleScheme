package com.ihorak.truffle.node.exprs.core.comperison;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.exprs.core.BinaryBooleanOperationNode;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;

import java.math.BigInteger;

public abstract class LessThenEqualBinaryNode extends BinaryBooleanOperationNode {

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

    @TruffleBoundary
    @Fallback
    protected boolean fallback(Object left, Object right) {
        throw new SchemeException("<=: contract violation\nexpected: real?\ngiven left: " + left + "\ngiven right: " + right, this);
    }
}
