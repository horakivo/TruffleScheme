package com.ihorak.truffle.node.builtin.core;

import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.builtin.polyglot.TranslateInteropExceptionNode;
import com.ihorak.truffle.runtime.SchemeList;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;

// Returns #t if value is the empty list, #f otherwise.
@GenerateUncached
public abstract class IsNullCoreNode extends SchemeNode {

    public abstract boolean execute(Object object);

    @Specialization
    protected boolean doList(SchemeList list) {
        return list.isEmpty;
    }

    @Specialization(guards = "interop.hasArrayElements(receiver)", limit = "getInteropCacheLimit()")
    protected boolean doForeignObject(Object receiver,
                                      @Cached TranslateInteropExceptionNode translateInteropExceptionNode,
                                      @CachedLibrary("receiver") InteropLibrary interop) {
        return getForeignArraySize(receiver, interop, translateInteropExceptionNode) == 0;
    }

    @Fallback
    protected boolean doObject(Object obj) {
        return false;
    }
}
