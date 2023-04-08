package com.ihorak.truffle.node.exprs.shared;

import com.ihorak.truffle.node.exprs.LimitedBuiltin;
import com.ihorak.truffle.node.polyglot.TranslateInteropExceptionNode;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemePair;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;


public abstract class ConsExprNode extends LimitedBuiltin {


    @Specialization(guards = "!list.isEmpty")
    protected SchemeList doNonEmptySchemeList(Object car, SchemeList list) {
        return new SchemeList(car, list, list.size + 1, false);
    }

    @Specialization(guards = "list.isEmpty")
    protected SchemeList doEmptySchemeList(Object car, SchemeList list) {
        return new SchemeList(car, SchemeList.EMPTY_LIST, 1, false);
    }

    @Specialization(guards = "interopLibrary.hasArrayElements(cdr)", limit = "getInteropCacheLimit()")
    protected SchemeList doInterop(Object car,
                                   Object cdr,
                                   @Cached ListNode listNode,
                                   @Cached TranslateInteropExceptionNode translateInteropExceptionNode,
                                   @CachedLibrary("cdr") InteropLibrary interopLibrary) {
        try {
            var size = (int) interopLibrary.getArraySize(cdr);
            var array = new Object[size + 1];
            array[0] = car;

            for (int i = 0; i < size; i++) {
                array[i + 1] = interopLibrary.readArrayElement(cdr, i);
            }

            return listNode.execute(array);

        } catch (InteropException e) {
            throw translateInteropExceptionNode.execute(e, cdr, "cons", null);
        }

    }

    @Fallback
    public SchemePair doSchemePair(Object car, Object cdr) {
        return new SchemePair(car, cdr);
    }

    public boolean isSchemeList(Object cdr) {
        return cdr instanceof SchemeList;
    }
}
