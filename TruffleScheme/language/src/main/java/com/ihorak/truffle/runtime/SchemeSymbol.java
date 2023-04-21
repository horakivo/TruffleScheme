package com.ihorak.truffle.runtime;


import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@ExportLibrary(InteropLibrary.class)
public record SchemeSymbol(String value) implements TruffleObject {

    @Override
    public String toString() {
        return "'" + this.value;
    }

    @ExportMessage
    @SuppressWarnings("static-method")
    boolean isString() {
        return true;
    }

    @ExportMessage
    String asString() {
        return value;
    }


}
