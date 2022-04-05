package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;

import java.math.BigInteger;

@NodeChild(value = "left")
@NodeChild(value = "right")
public abstract class MinusExprNode extends SchemeExpression {

    @Specialization(rewriteOn = ArithmeticException.class)
    protected long doLongs(long left, long right) {
        return Math.subtractExact(left, right);
    }

    @TruffleBoundary
    @Specialization(replaces = "doLongs")
    protected BigInteger doBigInts(BigInteger left, BigInteger right) {
        return left.subtract(right);
    }

    @Specialization
    protected double doDoubles(double left, double right) {
        return left - right;
    }

    @Override
    public String toString() {
        return "-";
    }
}
