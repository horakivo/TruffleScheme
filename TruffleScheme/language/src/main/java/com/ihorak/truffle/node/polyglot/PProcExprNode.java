package com.ihorak.truffle.node.polyglot;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.polyglot.object.PolyReadMemberNode;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Cached.Shared;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;

@NodeChild("languageId")
@NodeChild("procName")
public abstract class PProcExprNode extends SchemeExpression {

    //TODO shared zde na polyReadMemberNode?

    @Specialization(guards = {"procName.equals(cachedProcName)", "languageId.equals(cachedId)"}, limit = "2")
    protected Object findPolyglotProcedureCached(SchemeSymbol languageId,
                                                 SchemeSymbol procName,
                                                 @Cached("languageId") SchemeSymbol cachedId,
                                                 @Cached("procName") SchemeSymbol cachedProcName,
                                                 @Cached("getGlobalScope(cachedId)") Object cachedGlobalScope,
                                                 @Cached PolyReadMemberNode readMemberNode) {
        return readMemberNode.execute(cachedGlobalScope, cachedProcName);
    }

    @Specialization(replaces = "findPolyglotProcedureCached")
    protected Object findPolyglotProcedureUncached(SchemeSymbol languageId,
                                                   SchemeSymbol procName,
                                                   @Cached PolyReadMemberNode readMemberNode) {
        var globalScope = getGlobalScope(languageId);
        return readMemberNode.execute(globalScope, procName);
    }

    protected Object getGlobalScope(SchemeSymbol languageId) {
        return getLanguage().getGlobalScope(getContext().env, languageId.getValue());
    }
}
