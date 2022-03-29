package com.ihorak.truffle.node.special_form;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNode;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import java.util.List;

public class LetExprNode extends SchemeExpression {


    private final FrameDescriptor frameDescriptor;
    @Children private final SchemeExpression[] paramsValues;
    @Children private final SchemeExpression[] body;

    public LetExprNode(List<SchemeExpression> body, List<SchemeExpression> params, FrameDescriptor frameDescriptor) {
        this.body = body.toArray(SchemeExpression[]::new);
        this.paramsValues = params.toArray(SchemeExpression[]::new);
        this.frameDescriptor = frameDescriptor;
    }

    @ExplodeLoop
    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        Object[] arguments = getArguments(virtualFrame);
        var letFrame = Truffle.getRuntime().createVirtualFrame(arguments, frameDescriptor);

        for (int i = 0; i < body.length - 1; i++) {
            body[i].executeGeneric(letFrame);
        }

        return body[body.length - 1].executeGeneric(letFrame);
    }

    @ExplodeLoop
    private Object[] getArguments(VirtualFrame frame) {
        Object[] arguments = new Object[paramsValues.length + 1];
        arguments[0] = frame.materialize();

        int index = 1;
        for (SchemeExpression value : paramsValues) {
            arguments[index] = value.executeGeneric(frame);
            index++;
        }

        return arguments;
    }
}
