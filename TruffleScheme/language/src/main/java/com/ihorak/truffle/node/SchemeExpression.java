package com.ihorak.truffle.node;

import com.ihorak.truffle.type.*;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.source.SourceSection;

import java.math.BigInteger;
import java.util.List;

public abstract class SchemeExpression extends SchemeNode {

    private static final int NO_SOURCE = -1;
    private static final int UNAVAILABLE_SOURCE = -2;

    private int sourceCharIndex = NO_SOURCE;
    private int sourceLength;

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

    public BigInteger executeBigInt(VirtualFrame virtualFrame) throws UnexpectedResultException {
        return SchemeTypesGen.expectBigInteger(executeGeneric(virtualFrame));
    }

    public SchemeMacro executeMacro(VirtualFrame virtualFrame) throws UnexpectedResultException {
        return SchemeTypesGen.expectSchemeMacro(executeGeneric(virtualFrame));
    }

    // invoked by the parser to set the source
    public final void setSourceSection(int charIndex, int length) {
        assert sourceCharIndex == NO_SOURCE : "source must only be set once";
        if (charIndex < 0) {
            throw new IllegalArgumentException("charIndex < 0");
        } else if (length < 0) {
            throw new IllegalArgumentException("length < 0");
        }
        this.sourceCharIndex = charIndex;
        this.sourceLength = length;
    }
}
