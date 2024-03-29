package com.ihorak.truffle.node;

import com.ihorak.truffle.SchemeLanguageContext;
import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.node.builtin.polyglot.TranslateInteropExceptionNode;
import com.ihorak.truffle.runtime.SchemeTypes;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.interop.InteropException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.strings.TruffleString;

@TypeSystemReference(SchemeTypes.class)
public abstract class SchemeNode extends Node {

    protected final SchemeLanguageContext getContext() {
        return SchemeLanguageContext.get(this);
    }

    protected final SchemeTruffleLanguage getLanguage() {
        return SchemeTruffleLanguage.get(this);
    }

    protected final int getInteropCacheLimit() {
        return 2;
    }

    protected boolean areTruffleStringsEqual(TruffleString.EqualNode equalNode, TruffleString left, TruffleString right) {
        return equalNode.execute(left, right, SchemeTruffleLanguage.STRING_ENCODING);
    }

    // interop helper methods
    protected final int getForeignArraySize(Object receiver, InteropLibrary interop, TranslateInteropExceptionNode translateInteropExceptionNode) {
        try {
            return (int) interop.getArraySize(receiver);
        } catch (UnsupportedMessageException e) {
            throw translateInteropExceptionNode.execute(e, receiver);
        }
    }

    protected final Object readForeignArrayElement(Object receiver, int index, InteropLibrary interop, TranslateInteropExceptionNode translateInteropExceptionNode) {
        try {
            return interop.readArrayElement(receiver, index);
        } catch (InteropException e) {
            throw translateInteropExceptionNode.execute(e, receiver);
        }
    }

    protected static Object executeForeignProcedure(Object procedure, Object[] arguments, InteropLibrary interop, TranslateInteropExceptionNode translateInteropExceptionNode) {
        try {
            return interop.execute(procedure, arguments);
        } catch (InteropException e) {
            throw translateInteropExceptionNode.execute(e, procedure);
        }
    }
}
