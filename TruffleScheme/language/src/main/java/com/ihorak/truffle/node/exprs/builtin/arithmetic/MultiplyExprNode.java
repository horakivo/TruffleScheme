package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.node.exprs.builtin.BinaryOperationNode;
import com.oracle.truffle.api.dsl.Specialization;


public abstract class MultiplyExprNode extends BinaryOperationNode {

    @Specialization//(rewriteOn = ArithmeticException.class)
    public long doLong(long left, long right) {
        return Math.multiplyExact(left, right);
    }

//    @Specialization(replaces = "doLong")
//    public BigInteger doBigInt(BigInteger left, BigInteger right) {
//        return left.multiply(right);
//    }
}
