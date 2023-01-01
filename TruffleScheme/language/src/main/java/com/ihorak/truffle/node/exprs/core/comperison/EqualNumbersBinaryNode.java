package com.ihorak.truffle.node.exprs.core.comperison;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.exprs.core.BinaryBooleanOperationNode;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

import java.math.BigInteger;

public abstract class EqualNumbersBinaryNode extends BinaryBooleanOperationNode {

    @Specialization
    protected boolean equalLongs(long left, long right) {
        return left == right;
    }

    @CompilerDirectives.TruffleBoundary
    @Specialization
    protected boolean equalBigInts(BigInteger left, BigInteger right) {
        return left.compareTo(right) == 0;
    }

    @Specialization
    protected boolean equalDoubles(double left, double right) {
        return left == right;
    }

    @CompilerDirectives.TruffleBoundary
    @Fallback
    protected boolean fallback(Object left, Object right) {
        throw new SchemeException("=: contract violation\nexpected: number?\ngiven left: " + left + "\ngiven right: " + right, this);
    }
}