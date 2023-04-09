package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeBigInt;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;


@NodeChild(value = "number")
public abstract class NegateNumberExprNode extends SchemeExpression {

    @Specialization
    protected long doLong(long value) {
        return -value;
    }

    @TruffleBoundary
    @Specialization
    protected SchemeBigInt doBigInt(SchemeBigInt value) {
        return new SchemeBigInt(value.getValue().negate());
    }

    @Specialization
    protected double doDouble(double value) {
        return -value;
    }
}
