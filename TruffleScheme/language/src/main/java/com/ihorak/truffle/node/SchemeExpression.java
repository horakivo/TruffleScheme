package com.ihorak.truffle.node;

import com.ihorak.truffle.type.*;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;

import java.math.BigInteger;
import java.util.List;

public abstract class SchemeExpression extends SchemeNode {

    @CompilationFinal
    public boolean isTailRecursive = false;

    @CompilationFinal
    public boolean isSelfTailRecursive = false;

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

    public SchemeCell executeList(VirtualFrame virtualFrame) throws UnexpectedResultException {
        return SchemeTypesGen.expectSchemeCell(executeGeneric(virtualFrame));
    }

    public BigInteger executeBigInt(VirtualFrame virtualFrame) throws UnexpectedResultException {
        return SchemeTypesGen.expectBigInteger(executeGeneric(virtualFrame));
    }

    public SchemeMacro executeMacro(VirtualFrame virtualFrame) throws UnexpectedResultException {
        return SchemeTypesGen.expectSchemeMacro(executeGeneric(virtualFrame));
    }

    public void setTailRecursiveAsTrue() {
        this.isTailRecursive = true;
    }

//    public void setSelfTailRecursive(List<SchemeSymbol> currentlyDefiningProcedures) {
//        this.isSelfTailRecursive = false;
//    }

    public boolean isTailRecursive() {
        return isTailRecursive;
    }
}
