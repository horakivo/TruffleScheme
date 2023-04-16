package com.ihorak.truffle.node.builtin.core.arithmetic;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.builtin.BinaryObjectOperationNode;
import com.ihorak.truffle.runtime.SchemeBigInt;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;


public abstract class PlusBinaryNode extends BinaryObjectOperationNode {

    @Specialization(rewriteOn = ArithmeticException.class)
    protected long addLongs(long left, long right) {
        return Math.addExact(left, right);
    }

    @TruffleBoundary
    @Specialization(replaces = "addLongs")
    protected SchemeBigInt addBigInts(SchemeBigInt left, SchemeBigInt right) {
        return new SchemeBigInt(left.getValue().add(right.getValue()));
    }

    @Specialization
    protected double addDoubles(double left, double right) {
        return left + right;
    }

    @Fallback
    protected Object fallback(Object left, Object right) {
        throw SchemeException.contractViolation(this, "+", "number?", left, right);
    }

    @Override
    public String toString() {
        return "+";
    }
}
