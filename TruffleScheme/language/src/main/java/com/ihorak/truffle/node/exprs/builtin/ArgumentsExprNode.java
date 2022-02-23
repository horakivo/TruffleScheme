package com.ihorak.truffle.node.exprs.builtin;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.dsl.NodeField;


@NodeField(name = "arguments", type = SchemeExpression[].class)
public abstract class ArgumentsExprNode extends SchemeExpression {
    public abstract SchemeExpression[] getArguments();

//    //TODO can i rewrite this into steams?
//    @Specialization(rewriteOn = UnexpectedResultException.class)
//    protected int[] evalIntArguments(VirtualFrame virtualFrame) throws UnexpectedResultException {
//        var arguments = getArguments();
//        int[] result = new int[arguments.length];
//        for (int i = 0; i < arguments.length; i++) {
//            int evaluatedInt = arguments[i].executeInt(virtualFrame);
//            result[i] = evaluatedInt;
//        }
//
//        return result;
//    }

//    @Specialization(replaces = "evalIntArguments")
//    protected double[] evalDoubleArguments(VirtualFrame virtualFrame) {
//        var arguments = getArguments();
//        double[] result = new double[arguments.length];
//        for (int i = 0; i < arguments.length; i++) {
//            try {
//                double evaluatedDouble = arguments[i].executeDouble(virtualFrame);
//                result[i] = evaluatedDouble;
//            } catch (UnexpectedResultException e) {
//                throw new SchemeException(this, "aaa");
//            }
//        }
//        return result;
//    }
}
