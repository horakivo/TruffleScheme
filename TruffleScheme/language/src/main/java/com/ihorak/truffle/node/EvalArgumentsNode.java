package com.ihorak.truffle.node;

import com.ihorak.truffle.SchemeException;
import com.oracle.truffle.api.frame.VirtualFrame;

import java.util.List;

public class EvalArgumentsNode extends SchemeExpression {


    @Children
    private SchemeExpression[] arguments;

    public EvalArgumentsNode(List<SchemeExpression> arguments) {
        this.arguments = arguments.toArray(SchemeExpression[]::new);
    }


    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        var evaluatedArguments = evaluateAllArgs(virtualFrame);



        var clazz = evaluatedArguments[0].getClass();
        if (isHomogenous(evaluatedArguments)) {
            if (long.class.equals(clazz) || Long.class.equals(clazz)) {
                return createLongArray(evaluatedArguments);
            } else if (double.class.equals(clazz) || Double.class.equals(clazz)) {
                throw new SchemeException("Not implemented yet");
            } else {
                throw new SchemeException("Not implemented yet");
            }
        } else {
            throw new SchemeException("Not implemented yet");
        }
    }

    private long[] createLongArray(Object[] evaluatedArguments) {
        long[] result = new long[evaluatedArguments.length];

        for (int i = 0; i < evaluatedArguments.length; i++) {
            result[i] = (long) evaluatedArguments[i];
        }

        return result;
    }

//    private double[] createDoubleArray(VirtualFrame virtualFrame) {
//        double[] result = new long[arguments.length];
//
//        for (int i = 0; i < arguments.length; i++) {
//            try {
//                result[i] = arguments[i].executeLong(virtualFrame);
//            } catch (UnexpectedResultException e) {
//                throw new SchemeException("EvalArgumentsNode: Unexpected result value. \n expected: " + long.class + "\n given: " + e.getResult().getClass());
//            }
//        }
//
//        return result;
//    }

    private boolean isHomogenous(Object[] array) {
        if (array.length == 0) {
            return true;
        }
        var clazz = array[0].getClass();
        for (int i = 1; i < array.length; i++) {
            if (!array[i].getClass().equals(clazz)) {
                return false;
            }
        }
        return true;
    }

    private Object[] evaluateAllArgs(VirtualFrame virtualFrame) {
        Object[] result = new Object[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            result[i] = arguments[i].executeGeneric(virtualFrame);
        }

        return result;
    }

}
