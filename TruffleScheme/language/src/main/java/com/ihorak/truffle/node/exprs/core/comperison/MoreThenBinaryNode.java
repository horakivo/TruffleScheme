package com.ihorak.truffle.node.exprs.core.comperison;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.exprs.bbuiltin.BinaryBooleanOperationNode;
import com.ihorak.truffle.type.SchemeBigInt;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;


public abstract class MoreThenBinaryNode extends BinaryBooleanOperationNode {

    @Specialization
    protected boolean doLong(long left, long right) {
        return left > right;
    }

    @Specialization
    protected boolean doDouble(double left, double right) {
        return left > right;
    }

    /*
     * 0: if the value of this BigInteger is equal to that of the BigInteger object passed as a parameter.
     * 1: if the value of this BigInteger is greater than that of the BigInteger object passed as a parameter.
     * -1: if the value of this BigInteger is less than that of the BigInteger object passed as a parameter/
     */
    @TruffleBoundary
    @Specialization
    protected boolean moreThenBigInts(SchemeBigInt left, SchemeBigInt right) {
        return left.compareTo(right) > 0;
    }

    @TruffleBoundary
    @Fallback
    protected boolean fallback(Object left, Object right) {
        throw SchemeException.contractViolation(this, ">", "real?", left, right);
    }
}
