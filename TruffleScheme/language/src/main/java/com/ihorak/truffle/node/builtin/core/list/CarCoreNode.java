package com.ihorak.truffle.node.builtin.core.list;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.builtin.polyglot.ForeignToSchemeNode;
import com.ihorak.truffle.node.builtin.polyglot.TranslateInteropExceptionNode;
import com.ihorak.truffle.runtime.SchemeList;
import com.ihorak.truffle.runtime.SchemePair;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;

@GenerateUncached
public abstract class CarCoreNode extends SchemeNode {

    public abstract Object execute(Object object);

    @Specialization(guards = "!list.isEmpty")
    protected Object doList(SchemeList list) {
        return list.car;
    }

    @Specialization
    protected Object doPair(SchemePair pair) {
        return pair.first();
    }

    @Specialization(guards = "list.isEmpty")
    protected Object doEmptyList(SchemeList list) {
        throw SchemeException.contractViolation(this, "car", "pair? or list?", list);
    }

    @Specialization(guards = "interopLib.hasArrayElements(receiver)", limit = "getInteropCacheLimit()")
    protected Object doForeignObject(Object receiver,
                                     @CachedLibrary("receiver") InteropLibrary interopLib,
                                     @Cached ForeignToSchemeNode foreignToSchemeNode,
                                     @Cached TranslateInteropExceptionNode translateInteropExceptionNode) {
        final var foreign = readForeignArrayElement(receiver, 0, interopLib, translateInteropExceptionNode);
        return foreignToSchemeNode.executeConvert(foreign);
    }

    @Fallback
    protected Object fallback(Object object) {
        throw SchemeException.contractViolation(this, "car", "pair? or list?", object);
    }
}
