package com.ihorak.truffle.node.exprs.core.comperison;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.exprs.core.BinaryBooleanOperationNode;
import com.ihorak.truffle.node.exprs.core.BinaryOperationNode;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

import java.math.BigInteger;

public abstract class MoreThenBinaryNode extends BinaryBooleanOperationNode {

    @Specialization
    protected boolean moreThenLongs(long left, long right) {
        return left > right;
    }

    /*
     * 0: if the value of this BigInteger is equal to that of the BigInteger object passed as a parameter.
     * 1: if the value of this BigInteger is greater than that of the BigInteger object passed as a parameter.
     * -1: if the value of this BigInteger is less than that of the BigInteger object passed as a parameter/
     */
    @Specialization
    protected boolean moreThenBigInts(BigInteger left, BigInteger right) {
        return left.compareTo(right) > 0;
    }

    @TruffleBoundary
    @Fallback
    protected boolean fallback(Object left, Object right) {
        throw SchemeException.contractViolation(this, ">", "real?", left, right);
    }
}
