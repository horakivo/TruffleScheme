package com.ihorak.truffle.type;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

import java.math.BigInteger;

@ExportLibrary(InteropLibrary.class)
public class SchemeBigInt implements TruffleObject {


    private static final long LONG_MAX_SAFE_DOUBLE = 9007199254740991L; // 2 ** 53 - 1  // 53 = size of double mantissa + 1
    private static final int INT_MAX_SAFE_FLOAT = 16777215; // 2 ** 24 - 1  // 24 = size of float mantissa + 1

    private static boolean inSafeDoubleRange(long l) {
        return l >= -LONG_MAX_SAFE_DOUBLE && l <= LONG_MAX_SAFE_DOUBLE;
    }

    private static boolean inSafeFloatRange(int i) {
        return i >= -INT_MAX_SAFE_FLOAT && i <= INT_MAX_SAFE_FLOAT;
    }

    private final BigInteger value;

    public SchemeBigInt(BigInteger value) {
        this.value = value;
    }

    public SchemeBigInt(long value) {
        this.value = BigInteger.valueOf(value);
    }

    public BigInteger getValue() {
        return value;
    }

    @TruffleBoundary
    public int compareTo(SchemeBigInt other) {
        return value.compareTo(other.value);
    }

    @Override
    @TruffleBoundary
    public String toString() {
        return value.toString();
    }

    @Override
    @TruffleBoundary
    public boolean equals(Object obj) {
        if (obj instanceof SchemeBigInt) {
            return value.equals(((SchemeBigInt) obj).getValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    // Interop messages

    @ExportMessage
    boolean isNumber() {
        return fitsInLong();
    }

    @ExportMessage
    @TruffleBoundary
    boolean fitsInByte() {
        return value.bitLength() < Byte.SIZE;
    }

    @ExportMessage
    @TruffleBoundary
    boolean fitsInShort() {
        return value.bitLength() < Short.SIZE;
    }

    @ExportMessage
    @TruffleBoundary
    boolean fitsInInt() {
        return value.bitLength() < Integer.SIZE;
    }

    @ExportMessage
    @TruffleBoundary
    boolean fitsInLong() {
        return value.bitLength() < Long.SIZE;
    }

    @ExportMessage
    @TruffleBoundary
    boolean fitsInFloat() {
        return fitsInInt() && inSafeFloatRange(value.intValue());
    }

    @ExportMessage
    @TruffleBoundary
    boolean fitsInDouble() {
        return fitsInLong() && inSafeDoubleRange(value.longValue());
    }

    @ExportMessage
    @TruffleBoundary
    byte asByte() throws UnsupportedMessageException {
        if (fitsInByte()) {
            return value.byteValue();
        } else {
            throw UnsupportedMessageException.create();
        }
    }

    @ExportMessage
    @TruffleBoundary
    short asShort() throws UnsupportedMessageException {
        if (fitsInShort()) {
            return value.shortValue();
        } else {
            throw UnsupportedMessageException.create();
        }
    }

    @TruffleBoundary
    @ExportMessage
    int asInt() throws UnsupportedMessageException {
        try {
            return value.intValueExact();
        } catch (ArithmeticException e) {
            throw UnsupportedMessageException.create();
        }
    }

    @TruffleBoundary
    @ExportMessage
    long asLong() throws UnsupportedMessageException {
        try {
            return value.longValueExact();
        } catch (ArithmeticException e) {
            throw UnsupportedMessageException.create();
        }
    }

    @TruffleBoundary
    @ExportMessage
    float asFloat() throws UnsupportedMessageException {
        if (fitsInFloat()) {
            return value.floatValue();
        } else {
            throw UnsupportedMessageException.create();
        }
    }

    @TruffleBoundary
    @ExportMessage
    double asDouble() throws UnsupportedMessageException {
        if (fitsInDouble()) {
            return value.doubleValue();
        } else {
            throw UnsupportedMessageException.create();
        }
    }
}
