package com.ihorak.truffle.runtime;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.InvalidArrayIndexException;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnknownIdentifierException;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.profiles.BranchProfile;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;

@ExportLibrary(InteropLibrary.class)
public record SchemePair(
        Object first,
        Object second) implements TruffleObject {


    @TruffleBoundary
    public int size() {
        CompilerAsserts.neverPartOfCompilation();
        var currentPair = this;
        int size = 0;
        while (currentPair.second instanceof SchemePair pair) {
            size++;
            currentPair = pair;
        }

        //+2 since the last pair is doesn't have in second cell pair -> we need to count first + second
        return size + 2;
    }

    @Override
    public String toString() {
        return "(" + first + " . " + second + ")";
    }
    //----------------InteropLibrary messages–----------------------

    @ExportMessage
    boolean hasLanguage() {
        return true;
    }

    @ExportMessage
    @TruffleBoundary
    Object toDisplayString(@SuppressWarnings("unused") boolean allowSideEffects) {
        return "(" + first + " . " + second + ")";
    }

    @ExportMessage
    Class<? extends TruffleLanguage<?>> getLanguage() {
        return SchemeTruffleLanguage.class;
    }

    @ExportMessage
    boolean hasMembers() {
        return true;
    }

    @ExportMessage
    @TruffleBoundary
    Object readMember(String member) throws UnknownIdentifierException {
        if (member.equals("first")) {
            return first;
        } else if (member.equals("second")) {
            return second;
        }
        throw UnknownIdentifierException.create(member);
    }

    @ExportMessage
    boolean isMemberReadable(String member) {
        return member.equals("first") || member.equals("second");
    }

    @ExportMessage
    @TruffleBoundary
    Object getMembers(@SuppressWarnings("unused") boolean includeInternal) {
        return new FieldNames(new String[]{"first", "second"});
    }

    @ExportLibrary(InteropLibrary.class)
    static class FieldNames implements TruffleObject {

        private final String[] names;

        FieldNames(String[] names) {
            this.names = names;
        }

        @ExportMessage
        boolean hasArrayElements() {
            return true;
        }

        @ExportMessage
        boolean isArrayElementReadable(long index) {
            return index >= 0 && index < names.length;
        }

        @ExportMessage
        long getArraySize() {
            return names.length;
        }

        @ExportMessage
        Object readArrayElement(long index, @Cached BranchProfile error) throws InvalidArrayIndexException {
            if (!isArrayElementReadable(index)) {
                error.enter();
                throw InvalidArrayIndexException.create(index);
            }
            return names[(int) index];
        }
    }

}
