package com.ihorak.truffle.node.builtin.core.list;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.polyglot.ForeignToSchemeNode;
import com.ihorak.truffle.runtime.SchemeList;
import com.ihorak.truffle.runtime.SchemePair;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;

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

    @Specialization(guards = "interopLib.hasArrayElements(obj)", limit = "getInteropCacheLimit()")
    protected Object doForeignObject(Object obj,
                                     @CachedLibrary("obj") InteropLibrary interopLib,
                                     @Cached ForeignToSchemeNode foreignToSchemeNode) {
        try {
            var foreign = interopLib.readArrayElement(obj, 0);
            return foreignToSchemeNode.executeConvert(foreign);
        } catch (InteropException e) {
            throw SchemeException.interopException(e);
        }
    }

    @Fallback
    protected Object fallback(Object object) {
        throw SchemeException.contractViolation(this, "car", "pair? or list?", object);
    }
}
