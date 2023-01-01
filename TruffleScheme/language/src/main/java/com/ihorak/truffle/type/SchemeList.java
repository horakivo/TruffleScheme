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

import java.util.Iterator;

@ExportLibrary(InteropLibrary.class)
public class SchemeList implements Iterable<Object>, TruffleObject {

    public SchemeCell list;
    public SchemeCell bindingCell;
    public int size;
    @CompilationFinal
    public boolean isEmpty;

//    public SchemeList(Object firstObjectToBeAdded) {
//        this.list = new SchemeCell(firstObjectToBeAdded, SchemeCell.EMPTY_LIST);
//        this.bindingCell = this.list;
//        this.size = 1;
//    }
//
//    //not supporting empty list. Used for just internal purposes. Not for interpreter code!
//    public SchemeList(Object... objectsToBeAdded) {
//        this(objectsToBeAdded[0]);
//
//        for (int i = 1; i < objectsToBeAdded.length; i++) {
//            add(objectsToBeAdded[i]);
//        }
//    }

    public SchemeList() {
        CompilerDirectives.transferToInterpreterAndInvalidate();
        this.isEmpty = true;
        this.list = SchemeCell.EMPTY_LIST;
        this.size = 0;
    }

    public SchemeList(final SchemeCell list, final SchemeCell bindingCell, final int size, final boolean isEmpty) {
        this.list = list;
        this.bindingCell = bindingCell;
        this.size = size;
        this.isEmpty = isEmpty;
    }

    public SchemeList(Object... objects) {
        this();
        for (Object obj : objects) {
            add(obj);
        }
    }

    public void add(Object toBeAdded) {
        if (isEmpty) {
            this.list = new SchemeCell(toBeAdded, SchemeCell.EMPTY_LIST);
            this.bindingCell = this.list;
            this.size = 1;
            this.isEmpty = false;
        } else {
            var cell = new SchemeCell(toBeAdded, bindingCell.cdr);
            bindingCell.cdr = cell;
            this.bindingCell = cell;
            this.size++;
        }
    }

    public void addAll(SchemeList toBeAdded) {
        if (isEmpty) {
            this.list = toBeAdded.list;
            this.size = toBeAdded.size;
            this.bindingCell = toBeAdded.bindingCell;
            this.isEmpty = toBeAdded.isEmpty;
        } else {
            this.bindingCell.cdr = toBeAdded.list;
            this.bindingCell = toBeAdded.bindingCell;
            this.size += toBeAdded.size;
        }

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
