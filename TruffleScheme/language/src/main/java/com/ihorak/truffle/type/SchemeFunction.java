package com.ihorak.truffle.type;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.SchemeRootNode;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;

import java.util.List;

public class SchemeFunction {

    private final CallTarget callTarget;
    private final Integer expectedNumberOfArgs;

    public SchemeFunction(CallTarget callTarget, Integer expectedNumberOfArgs) {
        this.callTarget = callTarget;
        this.expectedNumberOfArgs = expectedNumberOfArgs;
    }

    public CallTarget getCallTarget() {
        return callTarget;
    }

    public Integer getExpectedNumberOfArgs() {
        return expectedNumberOfArgs;
    }

    public static SchemeFunction createFunction(List<SchemeExpression> schemeExpressions, SchemeCell parameters, FrameDescriptor frameDescriptor) {
        var callTarget = new SchemeRootNode(null, frameDescriptor, schemeExpressions).getCallTarget();
        return new SchemeFunction(callTarget, parameters.size());
    }

    public static SchemeFunction createBuiltinFunction(SchemeExpression schemeExpression, Integer expectedNumberOfArgs) {
        var rootNode = new SchemeRootNode(null, null, List.of(schemeExpression));
        var callTarget = Truffle.getRuntime().createCallTarget(rootNode);

        return new SchemeFunction(callTarget, expectedNumberOfArgs);
    }


}
