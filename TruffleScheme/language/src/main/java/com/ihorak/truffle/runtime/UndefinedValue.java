package com.ihorak.truffle.runtime;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@ExportLibrary(InteropLibrary.class)
public class UndefinedValue implements TruffleObject {

    public static final UndefinedValue SINGLETON = new UndefinedValue();

    private UndefinedValue() {
    }

    @Override
    public String toString() {
        return "undefined";
    }


    //----------------InteropLibrary messages–----------------------


    @ExportMessage
    boolean hasLanguage() {
        return true;
    }

    @ExportMessage
    Class<? extends TruffleLanguage<?>> getLanguage() {
        return SchemeTruffleLanguage.class;
    }

    @ExportMessage
    boolean isNull() {
        return true;
    }

    @ExportMessage
    Object toDisplayString(@SuppressWarnings("unused") boolean allowSideEffects) {
        return "undefined";
    }

}
