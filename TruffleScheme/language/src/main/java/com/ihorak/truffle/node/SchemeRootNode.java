package com.ihorak.truffle.node;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.runtime.SchemeSymbol;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.SourceSection;

import java.util.List;

public class SchemeRootNode extends RootNode {

    @Children
    public final SchemeExpression[] schemeExpressions;

    private final SchemeSymbol name;

    private final SourceSection sourceSection;

    public SchemeRootNode(SchemeTruffleLanguage language, FrameDescriptor frameDescriptor, List<SchemeExpression> schemeExpressions, SchemeSymbol name, SourceSection sourceSection) {
        super(language, frameDescriptor);
        this.name = name;
        this.sourceSection = sourceSection;
        this.schemeExpressions = schemeExpressions.toArray(SchemeExpression[]::new);
    }

    @Override
    @ExplodeLoop
    public Object execute(VirtualFrame frame) {
        for (int i = 0; i < schemeExpressions.length - 1; i++) {
            schemeExpressions[i].executeGeneric(frame);
        }
        //return last element
        return schemeExpressions[schemeExpressions.length - 1].executeGeneric(frame);
    }

    @Override
    public SourceSection getSourceSection() {
        return sourceSection;
    }

    @Override
    public String getName() {
        return name.value();
    }

    @Override
    public String toString() {
        return name.value();
    }
}