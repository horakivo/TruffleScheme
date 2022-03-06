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

    public static SchemeCell EMPTY_LIST = new SchemeCell(null, null);

    public Object car;
    public Object cdr;


    public SchemeCell(Object car, Object cdr) {
        this.car = car;
        this.cdr = cdr;
    }

    public boolean isEmpty() {
        return this == EMPTY_LIST;
    }

    public SchemeCell cons(Object car, Object cdr) {
        return new SchemeCell(car, cdr);
    }

    public boolean isList() {
        if (this == EMPTY_LIST) {
            return true;
        }

        if (cdr instanceof SchemeCell) {
            return ((SchemeCell) cdr).isList();
        }

        return false;
    }

    public int size() {
        if (isList()) {
            if (isEmpty()) {
                return 0;
            }

            int length = 1;
            SchemeCell schemeList = (SchemeCell) this.cdr;
            while (!schemeList.isEmpty()) {
                length++;
                schemeList = (SchemeCell) schemeList.cdr;
            }

            return length;
        } else {
            //is pair therefore always return 2
            return 2;
        }
    }

    @Override
    public String toString() {
        if (this.isList()) {
            return toStringList();
        } else {
            return toStringPair();
        }
    }

    private String toStringPair() {
        return '(' +
                car.toString() +
                " . " +
                cdr.toString() +
                ')';
    }

    private String toStringList() {
        if (isEmpty()) {
            return "()";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('(');


        var currentList = this;
        while (!currentList.isEmpty()) {
            sb.append(currentList.car);
            sb.append(" ");
            currentList = (SchemeCell) currentList.cdr;
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append(')');

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchemeCell objects = (SchemeCell) o;
        return Objects.equals(car, objects.car) && Objects.equals(cdr, objects.cdr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(car, cdr);
    }

    //TODO is there any solution which will unify the iteration over this?
    @Override
    public Iterator<Object> iterator() {
        if (isList()) {
            return new Iterator<>() {
                private SchemeCell cell = SchemeCell.this;

                @Override
                public boolean hasNext() {
                    return cell != EMPTY_LIST;
                }

                @Override
                public Object next() {
                    Object toReturn = cell.car;
                    cell = (SchemeCell) cell.cdr;
                    return toReturn;
                }
            };
        } else {
            return convertToArrayList().iterator();
        }
    }

    public List<Object> convertToArrayList() {
        List<Object> result = new ArrayList<>();

        for (Object obj : this) {
            result.add(obj);
        }

        return result;
    }

    //TODO just for list for now
    public Object get(int index) {
        int size = size();
        if (index >= size) {
            throw new ArrayIndexOutOfBoundsException("SchemeCell out of bounds. Index: " + index + ". Size: " + size);
        }

        SchemeCell currentList = this;
        for (int i = 0; i < index; i++) {
            currentList = (SchemeCell) currentList.cdr;
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
