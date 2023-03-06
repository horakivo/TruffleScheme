package com.ihorak.truffle.node.exprs.core.comperison;

import com.ihorak.truffle.node.exprs.core.BinaryBooleanOperationNode;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class EqualBinaryNode extends BinaryBooleanOperationNode {


    @Specialization
    protected boolean doSchemeSymbols(SchemeSymbol left, SchemeSymbol right) {
        return left.equals(right);
    }

    @Specialization
    protected boolean doLongs(long left, long right) {
        return left == right;
    }

    @Specialization
    protected boolean doSchemeSymbolAndLong(long left, SchemeSymbol right) {
        return false;
    }

    @Specialization
    protected boolean doLongAndSchemeSymbol(SchemeSymbol left, long right) {
        return false;
    }


    @Fallback
    protected boolean fallback(Object left, Object right) {
       // System.out.println("left: " + left + " right: " + right);
        return false;
    }
}
