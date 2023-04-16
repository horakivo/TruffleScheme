package com.ihorak.truffle.node.cast;

import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.type.PrimitiveProcedure;
import com.ihorak.truffle.type.SchemeBigInt;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemePair;
import com.ihorak.truffle.type.SchemeSymbol;
import com.ihorak.truffle.type.UndefinedValue;
import com.ihorak.truffle.type.UserDefinedProcedure;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.profiles.ConditionProfile;
import com.oracle.truffle.api.strings.TruffleString;


public abstract class BooleanCastNode extends SchemeNode {

    public abstract boolean executeBoolean(Object value);

    @Specialization
    protected boolean doBoolean(boolean value) {
        return value;
    }

    @Specialization
    protected boolean doLong(long value) {
        return true;
    }

    @Specialization
    protected boolean doDouble(double value) {
        return true;
    }

    @Specialization
    protected boolean doBigInteger(SchemeBigInt value) {
        return true;
    }

    @Specialization
    protected boolean doSchemeList(SchemeList value) {
        return true;
    }

    @Specialization
    protected boolean doTruffleString(TruffleString value) {
        return true;
    }

    @Specialization
    protected boolean doSchemeSymbol(SchemeSymbol value) {
        return true;
    }

    @Specialization
    protected boolean doUndefinedValue(UndefinedValue value) {
        return true;
    }

    @Specialization
    protected boolean doUserDefineProcedure(UserDefinedProcedure value) {
        return true;
    }

    @Specialization
    protected boolean doPrimitiveProcedure(PrimitiveProcedure procedure) {
        return true;
    }

    @Specialization
    protected boolean doSchemePair(SchemePair value) {
        return true;
    }

    @Specialization(limit = "getInteropCacheLimit()")
    protected boolean doForeignObject(Object object,
                                      @Cached("createCountingProfile()") ConditionProfile isBooleanProfile,
                                      @CachedLibrary("object") InteropLibrary interop) {
        if (isBooleanProfile.profile(interop.isBoolean(object))) {
            try {
                return interop.asBoolean(object);
            } catch (UnsupportedMessageException e) {
                // it concurrently stopped being boolean
                return true;
            }
        }

        return true;
    }
}
