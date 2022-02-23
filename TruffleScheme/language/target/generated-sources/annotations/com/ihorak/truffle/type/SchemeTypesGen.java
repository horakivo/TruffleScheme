// CheckStyle: start generated
package com.ihorak.truffle.type;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

@GeneratedBy(SchemeTypes.class)
public final class SchemeTypesGen extends SchemeTypes {

    protected SchemeTypesGen() {
    }

    public static boolean isLong(Object value) {
        return value instanceof Long;
    }

    public static long asLong(Object value) {
        assert value instanceof Long : "SchemeTypesGen.asLong: long expected";
        return (long) value;
    }

    public static long expectLong(Object value) throws UnexpectedResultException {
        if (value instanceof Long) {
            return (long) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static boolean isDouble(Object value) {
        return value instanceof Double;
    }

    public static double asDouble(Object value) {
        assert value instanceof Double : "SchemeTypesGen.asDouble: double expected";
        return (double) value;
    }

    public static double expectDouble(Object value) throws UnexpectedResultException {
        if (value instanceof Double) {
            return (double) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static boolean isBoolean(Object value) {
        return value instanceof Boolean;
    }

    public static boolean asBoolean(Object value) {
        assert value instanceof Boolean : "SchemeTypesGen.asBoolean: boolean expected";
        return (boolean) value;
    }

    public static boolean expectBoolean(Object value) throws UnexpectedResultException {
        if (value instanceof Boolean) {
            return (boolean) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static boolean isString(Object value) {
        return value instanceof String;
    }

    public static String asString(Object value) {
        assert value instanceof String : "SchemeTypesGen.asString: String expected";
        return (String) value;
    }

    public static String expectString(Object value) throws UnexpectedResultException {
        if (value instanceof String) {
            return (String) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static boolean isSchemeCell(Object value) {
        return value instanceof SchemeCell;
    }

    public static SchemeCell asSchemeCell(Object value) {
        assert value instanceof SchemeCell : "SchemeTypesGen.asSchemeCell: SchemeCell expected";
        return (SchemeCell) value;
    }

    public static SchemeCell expectSchemeCell(Object value) throws UnexpectedResultException {
        if (value instanceof SchemeCell) {
            return (SchemeCell) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static boolean isSchemeFunction(Object value) {
        return value instanceof SchemeFunction;
    }

    public static SchemeFunction asSchemeFunction(Object value) {
        assert value instanceof SchemeFunction : "SchemeTypesGen.asSchemeFunction: SchemeFunction expected";
        return (SchemeFunction) value;
    }

    public static SchemeFunction expectSchemeFunction(Object value) throws UnexpectedResultException {
        if (value instanceof SchemeFunction) {
            return (SchemeFunction) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static double expectImplicitDouble(int state, Object value) throws UnexpectedResultException {
        if ((state & 0b1) != 0 && value instanceof Double) {
            return (double) value;
        } else if ((state & 0b10) != 0 && value instanceof Long) {
            return convertLongToDouble((long) value);
        } else {
            throw new UnexpectedResultException(value);
        }
    }

    public static boolean isImplicitDouble(int state, Object value) {
        return ((state & 0b1) != 0 && value instanceof Double)
             || ((state & 0b10) != 0 && value instanceof Long);
    }

    public static boolean isImplicitDouble(Object value) {
        return value instanceof Double
             || value instanceof Long;
    }

    public static double asImplicitDouble(int state, Object value) {
        if (CompilerDirectives.inInterpreter()) {
            return asImplicitDouble(value);
        }
        if ((state & 0b1) != 0 && value instanceof Double) {
            return (double) value;
        } else if ((state & 0b10) != 0 && value instanceof Long) {
            return convertLongToDouble((long) value);
        } else {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw new IllegalArgumentException("Illegal implicit source type.");
        }
    }

    public static double asImplicitDouble(Object value) {
        if (value instanceof Double) {
            return (double) value;
        } else if (value instanceof Long) {
            return convertLongToDouble((long) value);
        } else {
            throw new IllegalArgumentException("Illegal implicit source type.");
        }
    }

    public static int specializeImplicitDouble(Object value) {
        if (value instanceof Double) {
            return 0b1;
        } else if (value instanceof Long) {
            return 0b10;
        } else {
            return 0;
        }
    }

}
