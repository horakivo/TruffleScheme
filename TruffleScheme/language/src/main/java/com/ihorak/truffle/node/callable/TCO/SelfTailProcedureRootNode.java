package com.ihorak.truffle.node.callable.TCO;

import java.util.List;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.SchemeRootNode;
import com.ihorak.truffle.node.callable.TCO.loop_nodes.TailRecursiveCallLoopNode;
import com.ihorak.truffle.node.exprs.ReadProcedureArgsExprNode;
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNode;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.*;
import com.oracle.truffle.api.source.SourceSection;

public class SelfTailProcedureRootNode extends SchemeRootNode {

    @Child
    private LoopNode loop;

    @Children
    private final SchemeExpression[] writeArgumentsExprs;

    private final int resultIndex;

    public SelfTailProcedureRootNode(SchemeSymbol name, SchemeTruffleLanguage language, FrameDescriptor frameDescriptor,
                                     List<SchemeExpression> schemeExpressions, List<SchemeExpression> writeArgumentsExprs, int resultIndex, SourceSection sourceSection) {
        super(language, frameDescriptor, schemeExpressions, name, sourceSection);
        this.writeArgumentsExprs = writeArgumentsExprs.toArray(SchemeExpression[]::new);
        this.resultIndex = resultIndex;
        this.loop = Truffle.getRuntime().createLoopNode(new TailRecursiveCallLoopNode(this.schemeExpressions));
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
