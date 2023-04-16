package com.ihorak.truffle.type;

import com.oracle.truffle.api.dsl.ImplicitCast;
import com.oracle.truffle.api.dsl.TypeSystem;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.strings.TruffleString;

// we have 11 data types
@TypeSystem({long.class, double.class, SchemeBigInt.class, boolean.class, TruffleString.class, SchemeList.class, SchemePair.class, SchemeSymbol.class, UserDefinedProcedure.class, PrimitiveProcedure.class, UndefinedValue.class})
public abstract class SchemeTypes {

    @ImplicitCast
    @TruffleBoundary
    public static SchemeBigInt convertLongToBigInt(long value) {
        return new SchemeBigInt(value);
    }

    @ImplicitCast
    public static double convertLongToDouble(long value) {
        return value;
    }

}
