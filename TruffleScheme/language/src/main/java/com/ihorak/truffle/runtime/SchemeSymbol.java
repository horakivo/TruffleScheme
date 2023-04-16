package com.ihorak.truffle.runtime;


import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

import java.util.Objects;

@ExportLibrary(InteropLibrary.class)
public class SchemeSymbol implements TruffleObject {

    private final String value;

    public SchemeSymbol(String name) {
        this.value = name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "'" + this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchemeSymbol that = (SchemeSymbol) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
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
