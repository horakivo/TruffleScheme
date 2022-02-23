package com.ihorak.truffle.node.special_form;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import java.util.List;

public class LetExprNode extends SchemeExpression {


    private final FrameDescriptor frameDescriptor;

    @SuppressWarnings("FieldMayBeFinal")
    @Children
    private SchemeExpression[] body;

    public LetExprNode(List<SchemeExpression> body, FrameDescriptor frameDescriptor) {
        this.body = body.toArray(SchemeExpression[]::new);
        this.frameDescriptor = frameDescriptor;
    }

    @ExplodeLoop
    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        var letFrame = Truffle.getRuntime().createVirtualFrame(new Object[]{virtualFrame}, frameDescriptor);

        for (int i = 0; i < body.length - 1; i++) {
            body[i].executeGeneric(letFrame);
        }

        return body[body.length - 1].executeGeneric(letFrame);
    }
}
