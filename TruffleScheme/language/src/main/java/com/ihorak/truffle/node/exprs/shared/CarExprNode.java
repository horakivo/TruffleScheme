package com.ihorak.truffle.node.exprs.shared;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.exprs.GivenNumberOfArgsBuiltin;
import com.ihorak.truffle.node.interop.ForeignToSchemeNode;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemePair;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;

public abstract class CarExprNode extends GivenNumberOfArgsBuiltin {


    @Specialization(guards = "!list.isEmpty")
    protected Object doSchemeList(SchemeList list) {
        return list.car;
    }

    @Specialization
    protected Object doPairCar(SchemePair pair) {
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
