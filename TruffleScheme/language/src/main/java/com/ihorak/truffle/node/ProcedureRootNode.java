package com.ihorak.truffle.node;

import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.RootNode;

import java.util.List;

public class ProcedureRootNode extends RootNode {

    @SuppressWarnings("FieldMayBeFinal")
    @Children
    private SchemeExpression[] expressions;

    public ProcedureRootNode(TruffleLanguage<?> language, FrameDescriptor frameDescriptor, List<SchemeExpression> schemeExpressions) {
        super(language, frameDescriptor);
        this.expressions = schemeExpressions.toArray(SchemeExpression[]::new);
    }

    @Override
    @ExplodeLoop
    public Object execute(VirtualFrame frame) {
        for (int i = 0; i < expressions.length - 1; i++) {
            expressions[i].executeGeneric(frame);
        }
        //return last element
        return expressions[expressions.length - 1].executeGeneric(frame);
    }
}
