package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.node.exprs.builtin.BuiltinExpression;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class PlusExprNode extends BuiltinExpression {

//    @Specialization(guards = "twoElements(arguments)")
//    public long addTwoLongs(long[] arguments) {
//        long result = 0;
//        for (long argument : arguments) {
//            result = Math.addExact(result, argument);
//        }
//
//        return result;
//    }

//    @ExplodeLoop
//    @Specialization(guards = "arguments.length == cachedLength")
//    public long addCachedLongs(long[] arguments,
//                               @Cached("arguments.length")  int cachedLength) {
//        long result = 0;
//        for (int n = 0; n < cachedLength; n++) {
//            result = Math.addExact(result, arguments[n]);
//        }
//
//        return result;
//    }

    @Specialization//(replaces ="addTwoLongs") //(rewriteOn = ArithmeticException.class)
    public long addAnyNumberOfLongs(long... arguments) {
        long result = 0;
        for (long argument : arguments) {
            result = Math.addExact(result, argument);
        }

        return result;
    }
//
//    @Specialization
//    public Object addObjects(Object[] arguments) {
//        if (arguments.length == 0) {
//            return 0L;
//        }
//        throw new SchemeException("Not implemented yet. Here will be the most general adding. Such as BigInt");
//    }

//    @Specialization(replaces = "doLong")
//    public BigInteger doBigInt(BigInteger left, BigInteger right) {
//        return left.add(right);
//    }
}
