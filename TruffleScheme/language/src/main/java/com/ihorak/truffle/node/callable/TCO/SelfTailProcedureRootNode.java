package com.ihorak.truffle.node.callable.TCO;

import java.util.List;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.SchemeRootNode;
import com.ihorak.truffle.node.callable.TCO.loop_nodes.TailRecursiveCallLoopNode;
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

    private final int argumentsIndex;

    public SelfTailProcedureRootNode(SchemeSymbol name, SchemeTruffleLanguage language, FrameDescriptor frameDescriptor,
                                     List<SchemeExpression> schemeExpressions, int argumentsIndex, SourceSection sourceSection) {
        super(language, frameDescriptor, schemeExpressions, name, sourceSection);
        this.argumentsIndex = argumentsIndex;
        this.loop = Truffle.getRuntime().createLoopNode(new TailRecursiveCallLoopNode(this.schemeExpressions));
    }

    @Override
    public Object execute(VirtualFrame frame) {
        frame.setObject(argumentsIndex, frame.getArguments().clone());
        return loop.execute(frame);
    }
}
