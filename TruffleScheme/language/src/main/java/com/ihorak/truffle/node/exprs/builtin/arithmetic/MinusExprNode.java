package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.SchemeException;
import com.ihorak.truffle.node.exprs.builtin.BuiltinExpression;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class MinusExprNode extends BuiltinExpression {

    @Specialization //(rewriteOn = ArithmeticException.class)
    public long subtractAnyNumberOfLongs(long[] arguments) {
        if (arguments.length == 0) {
            throw new SchemeException("-: arity mismatch; Expected number of arguments does not match the given number \n expected: at least 1 \n given: 0");
        }

        if (arguments.length == 1) {
            return -arguments[0];
        }

        var result = arguments[0];
        for (int i = 1; i < arguments.length; i++) {
            result = Math.subtractExact(result, arguments[i]);
        }

        return result;
    }

    @Override
    public String toString() {
        return "-";
    }


    //    @Specialization(replaces = "doLong")
//    public BigInteger doBigInt(BigInteger left, BigInteger right) {
//        return left.subtract(right);
//    }
}
