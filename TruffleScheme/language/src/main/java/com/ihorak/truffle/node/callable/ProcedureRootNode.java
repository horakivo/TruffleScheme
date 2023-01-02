package com.ihorak.truffle.node.callable;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.RootNode;

import java.util.List;

public class ProcedureRootNode extends RootNode {

    @Children
    private final SchemeExpression[] expressions;

    private final SchemeSymbol name;

    public ProcedureRootNode(SchemeSymbol name, TruffleLanguage<?> language, FrameDescriptor frameDescriptor,
                                     List<SchemeExpression> schemeExpressions) {
        super(language, frameDescriptor);
        this.expressions = schemeExpressions.toArray(SchemeExpression[]::new);
        this.name = name;
    }
    @Override
    @ExplodeLoop
    public Object execute(final VirtualFrame frame) {
        for (int i = 0; i < expressions.length - 1; i++) {
            expressions[i].executeGeneric(frame);
        }
        // return last element
        return expressions[expressions.length - 1].executeGeneric(frame);
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
