package com.ihorak.truffle.node.exprs.shared;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.exprs.GivenNumberOfArgsBuiltin;
import com.ihorak.truffle.node.polyglot.TranslateInteropExceptionNode;
import com.ihorak.truffle.type.SchemeList;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;

public abstract class LengthExprNode extends GivenNumberOfArgsBuiltin {

    @Specialization
    public long doSchemeList(SchemeList list) {
        return list.size;
    }

    @Specialization(guards = "interopLibrary.hasArrayElements(receiver)", limit = "getInteropCacheLimit()")
    public long doInterop(Object receiver,
                          @Cached TranslateInteropExceptionNode translateInteropExceptionNode,
                          @CachedLibrary("receiver") InteropLibrary interopLibrary) {
        try {
            return interopLibrary.getArraySize(receiver);
        } catch (InteropException e) {
            throw translateInteropExceptionNode.execute(e, receiver, "length", null);
        }
    }

    @Fallback
    protected void fallback(Object object) {
        throw new SchemeException("length: contract violation\nexpected: list?\ngiven: " + object, this);
    }


}

