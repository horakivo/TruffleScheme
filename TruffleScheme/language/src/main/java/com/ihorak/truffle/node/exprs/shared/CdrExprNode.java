package com.ihorak.truffle.node.exprs.shared;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.LimitedBuiltin;
import com.ihorak.truffle.node.interop.ForeignToSchemeNode;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemePair;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.profiles.BranchProfile;

public abstract class CdrExprNode extends LimitedBuiltin {


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
                                     @CachedLibrary("obj") InteropLibrary interopLib) {
        try {
            if (!interopLib.isArrayElementRemovable(obj, 0)) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                throw SchemeException.interopArrayElementIsNotRemovable();
            }
            interopLib.removeArrayElement(obj, 0);
            return obj;
        } catch (InteropException e) {
            throw SchemeException.interopException(e);
        }
    }

    @Fallback
    protected Object fallback(Object value) {
        throw SchemeException.contractViolation(this, "cdr", "pair? or list?", value);
    }
}
