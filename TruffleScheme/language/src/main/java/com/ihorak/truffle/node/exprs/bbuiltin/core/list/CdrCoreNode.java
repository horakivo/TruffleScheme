package com.ihorak.truffle.node.exprs.bbuiltin.core.list;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.exprs.bbuiltin.list.ListBuiltinNode;
import com.ihorak.truffle.node.polyglot.TranslateInteropExceptionNode;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemePair;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;

public abstract class CdrCoreNode extends SchemeNode {

    public abstract Object execute(Object object);

    @Specialization(guards = "!list.isEmpty")
    protected SchemeList doList(SchemeList list) {
        return list.cdr;
    }

    @Specialization
    protected Object doPair(SchemePair pair) {
        return pair.second();
    }

    @Specialization(guards = "list.isEmpty")
    protected SchemeList doEmptyList(SchemeList list) {
        throw SchemeException.contractViolation(this, "cdr", "pair? or list?", list);
    }

    @Specialization(guards = "interopLib.hasArrayElements(obj)", limit = "getInteropCacheLimit()")
    protected Object doForeignObject(Object obj,
                                     @Cached TranslateInteropExceptionNode translateInteropExceptionNode,
                                     @Cached ListBuiltinNode listNode,
                                     @CachedLibrary("obj") InteropLibrary interopLib) {
        try {
            var size = (int) interopLib.getArraySize(obj);
            Object[] array = new Object[size - 1];
            for (int i = 1; i < size; i++) {
                array[i - 1] = interopLib.readArrayElement(obj, i);
            }
            return listNode.execute(array);
        } catch (InteropException exception) {
            throw translateInteropExceptionNode.execute(exception, obj, "readArrayElement", null);
        }
    }

    @Fallback
    protected Object fallback(Object value) {
        throw SchemeException.contractViolation(this, "cdr", "pair? or list?", value);
    }
}
