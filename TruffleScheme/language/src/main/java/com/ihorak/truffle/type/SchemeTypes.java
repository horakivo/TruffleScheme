package com.ihorak.truffle.type;

import com.oracle.truffle.api.dsl.ImplicitCast;
import com.oracle.truffle.api.dsl.TypeSystem;

//BigInteger.class
@TypeSystem({long.class, double.class, boolean.class, String.class, SchemeCell.class, SchemeFunction.class})
public abstract class SchemeTypes {

//    @ImplicitCast
//    public static BigInteger convertLongToBigInt(long value) {
//        return BigInteger.valueOf(value);
//    }

    @ImplicitCast
    public static double convertLongToDouble(long value) {
        return value;
    }

    @ImplicitCast
    public static long convertIntToLong(int value) {
        return value;
    }

}
