package com.ihorak.truffle.node.exprs.builtin.logical;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;

import java.math.BigInteger;

@NodeChild(value = "left")
@NodeChild(value = "right")
public abstract class EqualExprNode extends SchemeExpression {

    @Specialization
    protected boolean equalLongs(long left, long right) {
        return left == right;
    }

    @Specialization
    protected boolean equalBigInts(BigInteger left, BigInteger right) {
        return left.compareTo(right) == 0;
    }
}
