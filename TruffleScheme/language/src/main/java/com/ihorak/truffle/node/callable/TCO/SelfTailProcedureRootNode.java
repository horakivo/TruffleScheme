package com.ihorak.truffle.node.callable.TCO;

import java.util.List;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.callable.TCO.loop_nodes.TailRecursiveCallLoopNode;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.*;

public class SelfTailProcedureRootNode extends RootNode {

    @Children
    private final SchemeExpression[] expressions;
    private final SchemeSymbol name;
    @Child
    private LoopNode loop;

    private final int argumentsIndex;

    public SelfTailProcedureRootNode(SchemeSymbol name, TruffleLanguage<?> language, FrameDescriptor frameDescriptor,
                                     List<SchemeExpression> schemeExpressions, int argumentsIndex) {
        super(language, frameDescriptor);
        this.expressions = schemeExpressions.toArray(SchemeExpression[]::new);
        this.name = name;
        this.argumentsIndex = argumentsIndex;
        this.loop = Truffle.getRuntime().createLoopNode(new TailRecursiveCallLoopNode(expressions, argumentsIndex, getFrameDescriptor(), getCallTarget()));
    }

    @Override
    public Object execute(VirtualFrame frame) {
        var arguments = frame.getArguments();
        arguments[2] = frame.materialize();
        frame.setObject(SchemeNode.TCO_ARGUMENT_SLOT, arguments);
        return loop.execute(frame);
    }

    @Override
    public String getName() {
        return name.getValue();
    }

    @Override
    public String toString() {
        return name.getValue();
    }
}
