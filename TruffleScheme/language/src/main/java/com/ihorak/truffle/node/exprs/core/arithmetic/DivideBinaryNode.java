package com.ihorak.truffle.node.exprs.core.arithmetic;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.core.BinaryOperationNode;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class DivideBinaryNode extends BinaryOperationNode {

    @Specialization
    protected double divide(double left, double right) {
        return left / right;
    }

    @Fallback
    protected double fallback(Object left, Object right) {
        throw SchemeException.contractViolation(this, "/", "number?", left, right);
    }

    @Override
    public String toString() {
        return "/";
    }
}
