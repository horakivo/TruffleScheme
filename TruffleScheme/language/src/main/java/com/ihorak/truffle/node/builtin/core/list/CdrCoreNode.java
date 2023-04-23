package com.ihorak.truffle.node.builtin.core.list;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.builtin.list.ListBuiltinNode;
import com.ihorak.truffle.node.builtin.polyglot.ForeignToSchemeNode;
import com.ihorak.truffle.node.builtin.polyglot.TranslateInteropExceptionNode;
import com.ihorak.truffle.runtime.SchemeList;
import com.ihorak.truffle.runtime.SchemePair;
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

    @Specialization(guards = "interopLib.hasArrayElements(receiver)", limit = "getInteropCacheLimit()")
    protected Object doForeignObject(Object receiver,
                                     @Cached TranslateInteropExceptionNode translateInteropExceptionNode,
                                     @Cached ListBuiltinNode listNode,
                                     @Cached ForeignToSchemeNode foreignToSchemeNode,
                                     @CachedLibrary("receiver") InteropLibrary interopLib) {
        var size = getForeignArraySize(receiver, interopLib, translateInteropExceptionNode);
        Object[] array = new Object[size - 1];
        for (int i = 1; i < size; i++) {
            var foreignObject = readForeignArrayElement(receiver, i, interopLib, translateInteropExceptionNode);
            array[i - 1] = foreignToSchemeNode.executeConvert(foreignObject);
        }
        return listNode.execute(array);
    }

    @Fallback
    protected Object fallback(Object value) {
        throw SchemeException.contractViolation(this, "cdr", "pair? or list?", value);
    }
}
