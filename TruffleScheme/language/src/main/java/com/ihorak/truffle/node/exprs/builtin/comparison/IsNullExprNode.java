package com.ihorak.truffle.node.exprs.builtin.comparison;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.polyglot.TranslateInteropExceptionNode;
import com.ihorak.truffle.type.SchemeList;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;

// Returns #t if value is the empty list, #f otherwise.
@NodeChild(value = "value")
public abstract class IsNullExprNode extends SchemeExpression {

    @Specialization
    protected boolean doList(SchemeList list) {
        return list.isEmpty;
    }

    @Specialization(guards = "interop.hasArrayElements(foreignObject)", limit = "getInteropCacheLimit()")
    protected boolean doForeignObject(Object foreignObject,
                                      @Cached TranslateInteropExceptionNode translateInteropExceptionNode,
                                      @CachedLibrary("foreignObject") InteropLibrary interop) {
        try {
            return interop.getArraySize(foreignObject) == 0;
        } catch (InteropException e) {
            throw translateInteropExceptionNode.execute(e, foreignObject, "null?", null);
        }
    }

    @Specialization
    protected boolean doObject(Object obj) {
        return false;
    }
}
