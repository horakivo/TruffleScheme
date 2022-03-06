package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;

import java.math.BigInteger;

@NodeChild(value = "left")
@NodeChild(value = "right")
public abstract class MinusTestNode extends SchemeExpression {

    @Specialization(rewriteOn = ArithmeticException.class)
    protected long subtractLongs(long left, long right) {
        return Math.subtractExact(left, right);
    }

    @Specialization(replaces = "subtractLongs")
    protected BigInteger subtractBigInts(BigInteger left, BigInteger right) {
        return left.subtract(right);
    }

    @Override
    public String toString() {
        return "-";
    }
}
