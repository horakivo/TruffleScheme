package com.ihorak.truffle.node;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;

import java.util.List;

public class SchemeRootNode extends RootNode {
    @SuppressWarnings("FieldMayBeFinal")
    @Children
    private SchemeExpression[] schemeExpressions;

    public SchemeRootNode(SchemeTruffleLanguage language, FrameDescriptor frameDescriptor, List<SchemeExpression> schemeExpressions) {
        super(language, frameDescriptor);
        this.schemeExpressions = schemeExpressions.toArray(SchemeExpression[]::new);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        Object result = null;
        for (SchemeExpression schemeExpression : schemeExpressions) {
            result = schemeExpression.executeGeneric(frame);
        }
        //we are returning the result of the last expression
        return result;
    }
}