package com.ihorak.truffle.node.callable.TCO;

import java.util.List;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.SchemeRootNode;
import com.ihorak.truffle.node.callable.TCO.loop_nodes.TailRecursiveLoopNode;
import com.ihorak.truffle.runtime.SchemeSymbol;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.*;
import com.oracle.truffle.api.source.SourceSection;

public class TailRecursiveRootNode extends SchemeRootNode {

    @Child
    private LoopNode loop;

    @Children
    private final SchemeExpression[] writeArgumentsExprs;

    private final int resultIndex;

    public TailRecursiveRootNode(SchemeSymbol name, SchemeTruffleLanguage language, FrameDescriptor frameDescriptor,
                                 List<SchemeExpression> schemeExpressions, List<SchemeExpression> writeArgumentsExprs, int resultIndex, SourceSection sourceSection) {
        super(language, frameDescriptor, schemeExpressions, name, sourceSection);
        this.writeArgumentsExprs = writeArgumentsExprs.toArray(SchemeExpression[]::new);
        this.resultIndex = resultIndex;
        this.loop = Truffle.getRuntime().createLoopNode(new TailRecursiveLoopNode(this.schemeExpressions, resultIndex));
    }

    @Override
    public Object execute(VirtualFrame frame) {
        prepareArgumentsForSelfTCO(frame);
        loop.execute(frame);

        return frame.getObject(resultIndex);
    }

    @ExplodeLoop
    private void prepareArgumentsForSelfTCO(VirtualFrame frame) {
        for (var expr : writeArgumentsExprs) {
            expr.executeGeneric(frame);
        }
    }
}
