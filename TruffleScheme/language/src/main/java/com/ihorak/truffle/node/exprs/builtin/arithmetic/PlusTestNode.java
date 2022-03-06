package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;

import java.math.BigInteger;

@NodeChild(value = "left")
@NodeChild(value = "right")
public abstract class PlusTestNode extends SchemeExpression {

    @Specialization(rewriteOn = ArithmeticException.class)
    protected long addLongs(long left, long right) {
        return Math.addExact(left, right);
    }

    @Specialization(replaces = "addLongs")
    protected BigInteger addBigInts(BigInteger left, BigInteger right) {
        return left.add(right);
    }

    @Specialization
    protected double addDoubles(double left, double right) {
        return left + right;
    }
}