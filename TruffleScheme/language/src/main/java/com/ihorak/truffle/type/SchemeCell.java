package com.ihorak.truffle.type;

import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.InvalidArrayIndexException;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.profiles.BranchProfile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@ExportLibrary(InteropLibrary.class)
public class SchemeCell implements Iterable<Object>, TruffleObject {

    public static final SchemeCell EMPTY_LIST = new SchemeCell(null, null);

    public Object car;
    public SchemeCell cdr;


    public SchemeCell(Object car, SchemeCell cdr) {
        this.car = car;
        this.cdr = cdr;
    }

    public boolean isEmpty() {
        return this == EMPTY_LIST;
    }

    public SchemeCell cons(Object car, SchemeCell cdr) {
        return new SchemeCell(car, cdr);
    }


    public int size() {

        if (isEmpty()) {
            return 0;
        }

        int length = 1;
        SchemeCell schemeList = this.cdr;
        while (!schemeList.isEmpty()) {
            length++;
            schemeList = schemeList.cdr;
        }

        return length;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "()";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('(');


        var currentList = this;
        while (!currentList.isEmpty()) {
            sb.append(currentList.car);
            sb.append(" ");
            currentList = currentList.cdr;
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append(')');

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchemeCell that = (SchemeCell) o;
        return Objects.equals(car, that.car) && Objects.equals(cdr, that.cdr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(car, cdr);
    }

    @Override
    public Iterator<Object> iterator() {
        return new Iterator<>() {
            private SchemeCell cell = SchemeCell.this;

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

    public Object get(int index) {
        int size = size();
        if (index >= size) {
            throw new ArrayIndexOutOfBoundsException("SchemeCell out of bounds. Index: " + index + ". Size: " + size);
        }

        SchemeCell currentList = this;
        for (int i = 0; i < index; i++) {
            currentList = currentList.cdr;
        }

        return currentList.car;
    }


    @ExportMessage
    boolean hasArrayElements() {
        return true;
    }

    @ExportMessage
    boolean isArrayElementReadable(long index) {
        return index >= 0 && index < size();
    }


    @ExportMessage
    long getArraySize() {
        return size();
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
