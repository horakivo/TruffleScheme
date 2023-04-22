package com.ihorak.truffle.node.builtin.core.list;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.builtin.polyglot.TranslateInteropExceptionNode;
import com.ihorak.truffle.runtime.SchemeList;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;

public abstract class LengthCoreNode extends SchemeNode {

    public abstract long execute(Object list);

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
    protected long fallback(Object object) {
        throw SchemeException.contractViolation(this, "length", "list?", object);
    }


}

