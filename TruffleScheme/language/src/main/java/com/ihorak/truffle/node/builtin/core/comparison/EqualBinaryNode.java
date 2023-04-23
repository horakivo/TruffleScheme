package com.ihorak.truffle.node.builtin.core.comparison;

import com.ihorak.truffle.node.builtin.BinaryBooleanOperationNode;
import com.ihorak.truffle.node.builtin.polyglot.PolyglotException;
import com.ihorak.truffle.runtime.PrimitiveProcedure;
import com.ihorak.truffle.runtime.SchemeBigInt;
import com.ihorak.truffle.runtime.SchemeList;
import com.ihorak.truffle.runtime.SchemePair;
import com.ihorak.truffle.runtime.SchemeSymbol;
import com.ihorak.truffle.runtime.UserDefinedProcedure;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.profiles.ConditionProfile;
import com.oracle.truffle.api.strings.TruffleString;

@NodeInfo(shortName = "equal?")
public abstract class EqualBinaryNode extends BinaryBooleanOperationNode {

    @Specialization
    protected boolean doSchemeSymbolAndLong(long left, SchemeSymbol right) {
        return false;
    }

    @Specialization
    protected boolean doLongs(long left, long right) {
        return left == right;
    }

    @Specialization
    protected boolean doDouble(double left, double right) {
        return left == right;
    }

    @TruffleBoundary
    @Specialization
    protected boolean doBigInt(SchemeBigInt left, SchemeBigInt right) {
        return left.compareTo(right) == 0;
    }

    @Specialization
    protected boolean doBoolean(boolean left, boolean right) {
        return left == right;
    }

    @Specialization
    protected boolean doStrings(TruffleString left, TruffleString right, @Cached TruffleString.EqualNode equalNode) {
        return areTruffleStringsEqual(equalNode, left, right);
    }

    //TODO truffle boundary?
    @Specialization
    protected boolean doSchemeSymbols(SchemeSymbol left, SchemeSymbol right) {
        return left.value().equals(right.value());
    }

    @Specialization
    protected boolean doSchemeList(SchemeList left,
                                   SchemeList right,
                                   @Cached EqualBinaryNode equalBinaryNode,
                                   @Cached ConditionProfile sameSizeProfile) {
        var leftSize = left.size;
        var rightSize = right.size;
        if (sameSizeProfile.profile(leftSize == rightSize)) {
            var currentLeft = left;
            var currentRight = right;
            while (currentLeft != SchemeList.EMPTY_LIST) {
                if (!equalBinaryNode.execute(currentLeft.car, currentRight.car)) {
                    return false;
                }
                currentLeft = currentLeft.cdr;
                currentRight = currentRight.cdr;
            }

            return true;
        } else {
            return false;
        }
    }

    @Specialization
    protected boolean doSchemePair(SchemePair left, SchemePair right,
                                   @Cached ConditionProfile sameSizeProfile,
                                   @Cached EqualBinaryNode equalBinaryNode) {
        final var leftSize = left.size();
        final var rightSize = right.size();

        if (sameSizeProfile.profile(leftSize == rightSize)) {
            var currentLeft = left;
            var currentRight = right;
            while (currentLeft.second() instanceof SchemePair) {
                if (!equalBinaryNode.execute(currentLeft.first(), currentRight.first())) {
                    return false;
                }
                currentLeft = (SchemePair) currentLeft.second();
                currentRight = (SchemePair) currentRight.second();
            }

            if (!equalBinaryNode.execute(currentLeft.first(), currentRight.first())) return false;
            if (!equalBinaryNode.execute(currentLeft.second(), currentRight.second())) return false;

            return true;
        } else {
            return false;
        }
    }

    @Specialization
    protected boolean doUserDefinedProcedure(UserDefinedProcedure left, UserDefinedProcedure right) {
        return left == right;
    }

    @Specialization
    protected boolean doUserDefinedProcedure(PrimitiveProcedure left, PrimitiveProcedure right) {
        return left == right;
    }

    @Fallback
    protected boolean fallback(Object left, Object right) {
        return left == right;
    }
}
