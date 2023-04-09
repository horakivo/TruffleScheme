package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeBigInt;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;


@NodeChild(value = "left")
@NodeChild(value = "right")
public abstract class ModuloExprNode extends SchemeExpression {

    @Specialization
    protected long doLongs(long left, long right) {
        return left % right;
    }

    @TruffleBoundary
    @Specialization
    protected SchemeBigInt doBigInteger(SchemeBigInt left, SchemeBigInt right) {
        return new SchemeBigInt(left.getValue().mod(right.getValue()));
    }

    @Fallback
    protected Object fallback(Object left, Object right) {
        throw SchemeException.contractViolation(this, "modulo", "integer?", left, right);
    }
}
