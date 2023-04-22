package com.ihorak.truffle.node.builtin.core.polyglot;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.strings.TruffleString;

public abstract class ReadForeignGlobalScopeCoreNode extends SchemeNode {
    public static final String POLYGLOT_READ_GLOBAL_SCOPE = "read-global-scope";


    public abstract Object execute(Object languageId, Object identifier);

    @Specialization(guards = {
            "areTruffleStringsEqual(equalNode, truffleLanguageId, cachedTruffleId)",
            "areTruffleStringsEqual(equalNode, truffleIdentifier, cachedTruffleIdentifier)"}, limit = "2")
    protected Object readForeignGlobalScopeCached(TruffleString truffleLanguageId,
                                                 TruffleString truffleIdentifier,
                                                 @Cached("truffleLanguageId") TruffleString cachedTruffleId,
                                                 @Cached("truffleIdentifier") TruffleString cachedTruffleIdentifier,
                                                 @Cached TruffleString.ToJavaStringNode toJavaStringNode,
                                                 @Cached("getGlobalScope(toJavaStringNode.execute(cachedTruffleId))") Object cachedGlobalScope,
                                                 @Cached TruffleString.EqualNode equalNode,
                                                 @Cached MemberNodes.ReadMember readMemberNode) {

        return readMemberNode.execute(cachedGlobalScope, cachedTruffleIdentifier);
    }

    @Specialization(replaces = "readForeignGlobalScopeCached")
    protected Object readForeignGlobalScopeUncached(TruffleString truffleLanguageId,
                                                   TruffleString truffleIdentifier,
                                                   @Cached TruffleString.ToJavaStringNode toJavaStringNode,
                                                   @Cached MemberNodes.ReadMember readMemberNode) {
        var globalScope = getGlobalScope(toJavaStringNode.execute(truffleLanguageId));
        return readMemberNode.execute(globalScope, truffleIdentifier);
    }

    @Fallback
    protected Object doThrow(Object languageId, Object identifier) {
        if (!(languageId instanceof TruffleString)) {
            throw SchemeException.contractViolation(this, POLYGLOT_READ_GLOBAL_SCOPE, "string?", languageId);
        } else if (!(identifier instanceof TruffleString)) {
            throw SchemeException.contractViolation(this, POLYGLOT_READ_GLOBAL_SCOPE, "string?", identifier);
        }

        throw SchemeException.shouldNotReachHere();
    }

    protected Object getGlobalScope(String languageId) {
        return getLanguage().getGlobalScope(getContext().env, languageId);
    }
}
