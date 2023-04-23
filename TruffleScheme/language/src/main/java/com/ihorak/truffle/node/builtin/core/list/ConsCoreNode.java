package com.ihorak.truffle.node.builtin.core.list;

import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.builtin.list.ListBuiltinNode;
import com.ihorak.truffle.node.builtin.polyglot.TranslateInteropExceptionNode;
import com.ihorak.truffle.runtime.SchemeList;
import com.ihorak.truffle.runtime.SchemePair;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;


public abstract class ConsCoreNode extends SchemeNode {

    public abstract Object execute(Object left, Object right);


    @Specialization(guards = "!list.isEmpty")
    protected SchemeList doNonEmptySchemeList(Object car, SchemeList list) {
        return new SchemeList(car, list, list.size + 1, false);
    }

    @Specialization(guards = "list.isEmpty")
    protected SchemeList doEmptySchemeList(Object car, SchemeList list) {
        return new SchemeList(car, SchemeList.EMPTY_LIST, 1, false);
    }

    @Specialization
    protected SchemePair doSchemePair(Object car, SchemePair pair) {
        return new SchemePair(car, pair, pair.size() + 1);
    }

    @Specialization(guards = "interopLibrary.hasArrayElements(receiverCdr)", limit = "getInteropCacheLimit()")
    protected SchemeList doInterop(Object car,
                                   Object receiverCdr,
                                   @Cached ListBuiltinNode listNode,
                                   @Cached TranslateInteropExceptionNode translateInteropExceptionNode,
                                   @CachedLibrary("receiverCdr") InteropLibrary interopLibrary) {
        var size = getForeignArraySize(receiverCdr, interopLibrary, translateInteropExceptionNode);
        var array = new Object[size + 1];
        array[0] = car;

        for (int i = 0; i < size; i++) {
            array[i + 1] = readForeignArrayElement(receiverCdr, i, interopLibrary, translateInteropExceptionNode);
        }

        return listNode.execute(array);
    }

    @Fallback
    public SchemePair doSchemePair(Object car, Object cdr) {
        return new SchemePair(car, cdr, 2);
    }

}
