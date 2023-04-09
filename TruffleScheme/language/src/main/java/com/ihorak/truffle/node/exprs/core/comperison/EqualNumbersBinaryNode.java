package com.ihorak.truffle.node.exprs.core.comperison;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.core.BinaryBooleanOperationNode;
import com.ihorak.truffle.type.SchemeBigInt;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;


public abstract class EqualNumbersBinaryNode extends BinaryBooleanOperationNode {

    @Specialization
    protected boolean equalLongs(long left, long right) {
        return left == right;
    }

    @TruffleBoundary
    @Specialization
    protected boolean equalBigInts(SchemeBigInt left, SchemeBigInt right) {
        return left.compareTo(right) == 0;
    }

    @Specialization
    protected boolean equalDoubles(double left, double right) {
        return left == right;
    }

    @Fallback
    protected boolean fallback(Object left, Object right) {
        throw SchemeException.contractViolation(this, "=", "number?", left, right);
    }
}
