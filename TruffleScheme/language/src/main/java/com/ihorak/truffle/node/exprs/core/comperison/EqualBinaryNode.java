package com.ihorak.truffle.node.exprs.core.comperison;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.exprs.core.BinaryOperationNode;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;

import java.math.BigInteger;

public abstract class EqualBinaryNode extends BinaryOperationNode {

    @Specialization
    protected boolean equalLongs(long left, long right) {
        return left == right;
    }

    @TruffleBoundary
    @Specialization
    protected boolean equalBigInts(BigInteger left, BigInteger right) {
        return left.compareTo(right) == 0;
    }

    @Specialization
    protected boolean equalDoubles(double left, double right) {
        return left == right;
    }

    @TruffleBoundary
    @Fallback
    protected Object fallback(Object left, Object right) {
        throw new SchemeException("=: contract violation\nexpected: real?\ngiven left: " + left + "\ngiven right: " + right, this);
    }
}
