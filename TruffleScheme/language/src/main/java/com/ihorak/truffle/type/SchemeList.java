package com.ihorak.truffle.type;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.InvalidArrayIndexException;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.profiles.BranchProfile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

@ExportLibrary(InteropLibrary.class)
public class SchemeList implements Iterable<Object>, TruffleObject {

    public SchemeCell list;
    @Nullable
    public SchemeCell bindingCell;
    public int size;
    public final boolean isEmpty;


    public SchemeList(final SchemeCell list, final @Nullable SchemeCell bindingCell, final int size, final boolean isEmpty) {
        this.list = list;
        this.bindingCell = bindingCell;
        this.size = size;
        this.isEmpty = isEmpty;
    }

    public Object get(int index) {
        return list.get(index);
    }

    public SchemeList cdr() {
        var newSize = size - 1;
        return new SchemeList(list.cdr, bindingCell, newSize, newSize == 0);
    }

    public Object car() {
        return list.car;
    }

    @Override
    public String toString() {
        return list.toString();
    }

    @NotNull
    @Override
    public Iterator<Object> iterator() {
        return list.iterator();
    }


    // INTEROP LIBRARY
    @ExportMessage
    boolean hasArrayElements() {
        return true;
    }

    @ExportMessage
    boolean isArrayElementReadable(long index) {
        return index >= 0 && index < size;
    }


    @ExportMessage
    long getArraySize() {
        return size;
    }

    @ExportMessage
    Object readArrayElement(long index, @Cached BranchProfile error) throws InvalidArrayIndexException {
        if (!isArrayElementReadable(index)) {
            error.enter();
            throw InvalidArrayIndexException.create(index);
        }
        return get((int) index);
    }
}
