package com.ihorak.truffle.runtime;

import com.ihorak.truffle.exceptions.SchemeException;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.InvalidArrayIndexException;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.profiles.BranchProfile;

import java.util.Iterator;

@ExportLibrary(InteropLibrary.class)
public class SchemeList implements Iterable<Object>, TruffleObject {

    public static final SchemeList EMPTY_LIST = new SchemeList(null, null, 0, true);

    public Object car;
    public SchemeList cdr;
    public final int size;
    public final boolean isEmpty;

    public SchemeList(Object car, SchemeList cdr, final int size, final boolean isEmpty) {
        this.car = car;
        this.cdr = cdr;
        this.size = size;
        this.isEmpty = isEmpty;
    }

    public Object get(int index) {
        if (index >= size) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw SchemeException.invalidIndexException(index, size);
        }

        SchemeList currentList = this;
        for (int i = 0; i < index; i++) {
            currentList = currentList.cdr;
        }

        return currentList.car;
    }

    //TODO useless here?
    @ExplodeLoop
    public SchemeList shallowClone() {
        if (isEmpty) return EMPTY_LIST;

        var head = new SchemeList(car, null, size, false);
        var tail = head;
        var original = this.cdr;

        for (int i = 1; i < size; i++) {
            var carValue = original.car instanceof SchemeList list ? list.shallowClone() : original.car;
            var cell = new SchemeList(carValue, null, tail.size - 1, false);
            tail.cdr = cell;
            tail = cell;
            original = original.cdr;
        }

        tail.cdr = EMPTY_LIST;

        return head;
    }


    @Override
    public String toString() {
        if (isEmpty) {
            return "()";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('(');


        var currentList = this;
        while (!currentList.isEmpty) {
            sb.append(currentList.car);
            sb.append(" ");
            currentList = currentList.cdr;
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append(')');

        return sb.toString();
    }

    @Override
    public Iterator<Object> iterator() {
        return new Iterator<>() {
            private SchemeList cell = SchemeList.this;

            @Override
            public boolean hasNext() {
                return cell != EMPTY_LIST;
            }

            @Override
            public Object next() {
                Object toReturn = cell.car;
                cell = cell.cdr;
                return toReturn;
            }
        };
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

        SchemeList currentList = this;
        for (int i = 0; i < index; i++) {
            currentList = currentList.cdr;
        }

        return currentList.car;
    }
}
