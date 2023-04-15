package com.ihorak.truffle.node.exprs.core.arithmetic;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.exprs.GivenNumberOfArgsBuiltin;
import com.ihorak.truffle.node.exprs.core.BinaryOperationNode;
import com.ihorak.truffle.type.SchemeBigInt;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;


public abstract class PlusCoreNode extends SchemeNode {

    public abstract Object execute(Object left, Object right);

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
