package com.ihorak.truffle.node.polyglot.object;

import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.polyglot.PolyglotException;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;


public abstract class PolyReadMemberNode extends SchemeNode {

    public abstract Object execute(Object foreignObject, SchemeSymbol identifier);

    @Specialization(limit = "getInteropCacheLimit()")
    protected Object doMember(Object foreignObject,
                              SchemeSymbol identifier,
                              @CachedLibrary("foreignObject") InteropLibrary interopLibrary) {
        try {
            return interopLibrary.readMember(foreignObject, identifier.getValue());
        } catch (InteropException e) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw PolyglotException.readMemberException(e, foreignObject, identifier, this);
        }
    }


}
