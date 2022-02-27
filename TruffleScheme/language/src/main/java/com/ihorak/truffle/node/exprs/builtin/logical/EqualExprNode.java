package com.ihorak.truffle.node.exprs.builtin.logical;

import com.ihorak.truffle.node.exprs.builtin.BinaryOperationNode;
import com.oracle.truffle.api.dsl.Specialization;

import java.math.BigInteger;

public abstract class EqualExprNode extends BinaryOperationNode {

    @Specialization
    protected boolean equalLongs(long left, long right) {
        return left == right;
    }


    @Specialization
    protected boolean equalBigInts(BigInteger left, BigInteger right) {
        return left.compareTo(right) == 0;
    }
}
