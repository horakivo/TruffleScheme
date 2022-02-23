package com.ihorak.truffle.node.exprs.builtin.logical;

import com.ihorak.truffle.SchemeException;
import com.ihorak.truffle.node.exprs.builtin.BuiltinExpression;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class LessThenOrEqualExprNode extends BuiltinExpression {


    @Specialization
    protected boolean lessThanEqualAnyNumberOfLongs(long[] arguments) {
        if (arguments.length == 0) {
            throw new SchemeException("<=: arity mismatch;\nthe expected number of arguments does not match the given number\nexpected: at least 1\ngiven: 0");
        }
        if (arguments.length == 1) {
            return true;
        }

        for (int i = 0; i < arguments.length - 1; i++) {
            if (arguments[i] > arguments[i + 1]) {
                return false;
            }
        }
        return true;
    }

}
