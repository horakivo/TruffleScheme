package com.ihorak.truffle.type;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.SchemeRootNode;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.MaterializedFrame;

import java.util.List;

public class SchemeFunction {

    private final CallTarget callTarget;
    private final Integer expectedNumberOfArgs;
    private MaterializedFrame parentFrame;

    public SchemeFunction(CallTarget callTarget, Integer expectedNumberOfArgs) {
        this.callTarget = callTarget;
        this.expectedNumberOfArgs = expectedNumberOfArgs;
    }

    public void setParentFrame(MaterializedFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public MaterializedFrame getParentFrame() {
        return parentFrame;
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
        var rootNode = new SchemeRootNode(null, new FrameDescriptor(), List.of(schemeExpression));

        return new SchemeFunction(rootNode.getCallTarget(), expectedNumberOfArgs);
    }
}