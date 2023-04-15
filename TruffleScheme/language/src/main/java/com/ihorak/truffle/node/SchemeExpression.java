package com.ihorak.truffle.node;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.type.*;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;
import com.oracle.truffle.api.strings.TruffleString;


public abstract class SchemeExpression extends SchemeNode {

    private static final int NO_SOURCE = -1;
    private static final int UNAVAILABLE_SOURCE = -2;

    private int sourceStartIndex = NO_SOURCE;
    private int sourceLength;

    private boolean hasRootTag = false;

    /**
     * The execute method when no specialization is possible. This is the most general case,
     * therefore it must be provided by all subclasses.
     */
    public abstract Object executeGeneric(VirtualFrame virtualFrame);

    public UserDefinedProcedure executeUserDefinedProcedure(VirtualFrame virtualFrame) throws UnexpectedResultException {
        return SchemeTypesGen.expectUserDefinedProcedure(executeGeneric(virtualFrame));
    }

    public boolean executeBoolean(VirtualFrame virtualFrame) throws UnexpectedResultException {
        return SchemeTypesGen.expectBoolean(executeGeneric(virtualFrame));
    }

    public long executeLong(VirtualFrame virtualFrame) throws UnexpectedResultException {
        return SchemeTypesGen.expectLong(executeGeneric(virtualFrame));
    }

    public double executeDouble(VirtualFrame virtualFrame) throws UnexpectedResultException {
        return SchemeTypesGen.expectDouble(executeGeneric(virtualFrame));
    }

    protected boolean areTruffleStringsEqual(TruffleString.EqualNode equalNode, TruffleString left, TruffleString right) {
        return equalNode.execute(left, right, SchemeTruffleLanguage.STRING_ENCODING);
    }

    @ExplodeLoop
    protected Object[] getProcedureArguments(final UserDefinedProcedure function, final SchemeExpression[] arguments, VirtualFrame frame) {
        Object[] args = new Object[arguments.length + 1];
        args[0] = function.getParentFrame();

        int index = 1;
        for (SchemeExpression expression : arguments) {
            args[index] = expression.executeGeneric(frame);
            index++;
        }

        return args;
    }

    @ExplodeLoop
    protected Object[] getPrimitiveProcedureArguments(final SchemeExpression[] arguments, VirtualFrame frame) {
        Object[] args = new Object[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            args[i] = arguments[i].executeGeneric(frame);
        }

        return args;
    }

    @ExplodeLoop
    protected Object[] getForeignProcedureArguments(SchemeExpression[] arguments, VirtualFrame parentFrame) {
        Object[] args = new Object[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            args[i] = arguments[i].executeGeneric(parentFrame);
        }

        return args;
    }


    @Override
    @TruffleBoundary
    public final SourceSection getSourceSection() {
        if (sourceStartIndex == NO_SOURCE) {
            // AST node without source
            return null;
        }
        RootNode rootNode = getRootNode();
        if (rootNode == null) {
            // not yet adopted yet
            return null;
        }
        SourceSection rootSourceSection = rootNode.getSourceSection();
        if (rootSourceSection == null) {
            return null;
        }
        Source source = rootSourceSection.getSource();
        if (sourceStartIndex == UNAVAILABLE_SOURCE) {
            if (hasRootTag && !rootSourceSection.isAvailable()) {
                return rootSourceSection;
            } else {
                return source.createUnavailableSection();
            }
        } else {
            return source.createSection(sourceStartIndex, sourceLength);
        }
    }

    public final void setUnavailableSourceSection() {
        assert sourceStartIndex == NO_SOURCE : "source must only be set once";
        this.sourceStartIndex = UNAVAILABLE_SOURCE;
    }

    // invoked by the parser to set the source
    public final void setSourceSection(int charIndex, int length) {
        assert sourceStartIndex == NO_SOURCE : "source must only be set once";
        if (charIndex < 0) {
            throw new IllegalArgumentException("charIndex < 0");
        } else if (length < 0) {
            throw new IllegalArgumentException("length < 0");
        }
        this.sourceStartIndex = charIndex;
        this.sourceLength = length;
    }

    public final int getSourceEndIndex() {
        return sourceStartIndex + sourceLength;
    }

    public int getSourceStartIndex() {
        return sourceStartIndex;
    }
}
