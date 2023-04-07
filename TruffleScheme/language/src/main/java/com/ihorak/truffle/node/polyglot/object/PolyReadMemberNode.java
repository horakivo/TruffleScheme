package com.ihorak.truffle.node.polyglot.object;

import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.interop.ForeignToSchemeNode;
import com.ihorak.truffle.node.polyglot.PolyglotException;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.strings.TruffleString;


public abstract class PolyReadMemberNode extends SchemeNode {

    public abstract Object execute(Object foreignObject, TruffleString identifier);

    @Specialization(limit = "getInteropCacheLimit()")
    protected Object doMember(Object foreignObject,
                              TruffleString identifier,
                              @Cached TruffleString.ToJavaStringNode toJavaStringNode,
                              @Cached ForeignToSchemeNode foreignToSchemeNode,
                              @CachedLibrary("foreignObject") InteropLibrary interopLibrary) {
        try {
            var result = interopLibrary.readMember(foreignObject, toJavaStringNode.execute(identifier));
            return foreignToSchemeNode.executeConvert(result);
        } catch (InteropException e) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw PolyglotException.readMemberException(e, foreignObject, toJavaStringNode.execute(identifier), this);
        }
    }


}
