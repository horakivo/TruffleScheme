package com.ihorak.truffle.node.exprs.bbuiltin.core;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.exprs.bbuiltin.BinaryObjectOperationNode;
import com.ihorak.truffle.type.SchemeBigInt;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;


public abstract class ModuloCoreNode extends BinaryObjectOperationNode {

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
