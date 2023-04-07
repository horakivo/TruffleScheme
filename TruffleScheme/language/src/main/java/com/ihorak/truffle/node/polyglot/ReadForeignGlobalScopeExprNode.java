package com.ihorak.truffle.node.polyglot;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.strings.TruffleString;
import com.ihorak.truffle.node.polyglot.MemberNodes.ReadMemberNode;

@NodeChild("languageId")
@NodeChild("procName")
public abstract class ReadForeignGlobalScopeExprNode extends SchemeExpression {

    //TODO shared zde na polyReadMemberNode?

    @Specialization(guards = {
            "areTruffleStringsEqual(equalNode, truffleLanguageId, cachedTruffleId)",
            "areTruffleStringsEqual(equalNode, truffleIdentifier, cachedTruffleIdentifier)"}, limit = "2")
    protected Object findPolyglotProcedureCached(TruffleString truffleLanguageId,
                                                 TruffleString truffleIdentifier,
                                                 @Cached("truffleLanguageId") TruffleString cachedTruffleId,
                                                 @Cached("truffleIdentifier") TruffleString cachedTruffleIdentifier,
                                                 @Cached TruffleString.ToJavaStringNode toJavaStringNode,
                                                 @Cached("getGlobalScope(toJavaStringNode.execute(cachedTruffleId))") Object cachedGlobalScope,
                                                 @Cached TruffleString.EqualNode equalNode,
                                                 @Cached ReadMemberNode readMemberNode) {
       return readMemberNode.execute(cachedGlobalScope, cachedTruffleIdentifier);
    }

    @Specialization(replaces = "findPolyglotProcedureCached")
    protected Object findPolyglotProcedureUncached(TruffleString truffleLanguageId,
                                                   TruffleString truffleIdentifier,
                                                   @Cached TruffleString.ToJavaStringNode toJavaStringNode,
                                                   @Cached ReadMemberNode readMemberNode) {
        var globalScope = getGlobalScope(toJavaStringNode.execute(truffleLanguageId));
        return readMemberNode.execute(globalScope, truffleIdentifier);
    }

    protected Object getGlobalScope(String languageId) {
        return getLanguage().getGlobalScope(getContext().env, languageId);
    }
}
