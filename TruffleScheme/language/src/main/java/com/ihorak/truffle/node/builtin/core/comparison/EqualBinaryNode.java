package com.ihorak.truffle.node.builtin.core.comparison;

import com.ihorak.truffle.node.builtin.BinaryBooleanOperationNode;
import com.ihorak.truffle.node.polyglot.PolyglotException;
import com.ihorak.truffle.runtime.SchemeBigInt;
import com.ihorak.truffle.runtime.SchemeList;
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

    //TODO truffle boundary?
    @Specialization
    protected boolean doSchemeSymbols(SchemeSymbol left, SchemeSymbol right) {
        return left.value().equals(right.value());
    }

    @Specialization
    protected boolean doUserDefinedProcedure(UserDefinedProcedure left, UserDefinedProcedure right) {
        return left == right;
    }

    @Specialization
    protected boolean doSchemeList(SchemeList left,
                                   SchemeList right,
                                   @Cached EqualBinaryNode equalBinaryNode,
                                   @Cached("createCountingProfile()") ConditionProfile sizeProfile) {
        var leftSize = left.size;
        var rightSize = right.size;
        if (sizeProfile.profile(leftSize != rightSize)) {
            return false;
        }

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
    }

    @Specialization(guards = {"interopLeft.hasArrayElements(left)", "interopRight.hasArrayElements(right)"}, limit = "getInteropCacheLimit()")
    protected boolean doForeignArray(Object left,
                                     Object right,
                                     @CachedLibrary("left") InteropLibrary interopLeft,
                                     @CachedLibrary("right") InteropLibrary interopRight) {

        throw new PolyglotException("Equal? is not supported for foreign arrays", this);
    }


    @Fallback
    protected boolean fallback(Object left, Object right) {
        return false;
    }
}
