package com.ihorak.truffle.type;

import com.oracle.truffle.api.dsl.ImplicitCast;
import com.oracle.truffle.api.dsl.TypeSystem;
import com.oracle.truffle.api.strings.TruffleString;

import java.math.BigInteger;

@TypeSystem({long.class, double.class, boolean.class, TruffleString.class, SchemeList.class, UserDefinedProcedure.class, BigInteger.class, SchemeMacro.class})
public abstract class SchemeTypes {

//    @ImplicitCast
//    public static BigInteger convertLongToBigInt(long value) {
//        return BigInteger.valueOf(value);
//    }

    @ImplicitCast
    public static double convertLongToDouble(long value) {
        return value;
    }

}
