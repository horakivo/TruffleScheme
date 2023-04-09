package com.ihorak.truffle.node.exprs.builtin.comparison;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeList;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;

@NodeChild(value = "value")
public abstract class IsNullExprNode extends SchemeExpression {

    @Specialization
    protected boolean doList(SchemeList list) {
        return list.isEmpty;
    }

    @Specialization(guards = "interop.accepts(foreignObject)", limit = "getInteropCacheLimit()")
    protected boolean doForeignObject(Object foreignObject, @CachedLibrary("foreignObject") InteropLibrary interop) {
        return interop.isNull(foreignObject);
    }

    @Specialization
    protected boolean doObject(Object obj) {
        return false;
    }
}
