package com.ihorak.truffle.node.builtin.polyglot;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

import static com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes.HAS_MEMBERS;
import static com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes.IS_MEMBER_EXISTING;

public abstract class IsMemberExistingBuiltinNode extends AlwaysInlinableProcedureNode {

    @Specialization(guards = "arguments.length == 2")
    protected boolean isMemberExisting(Object[] arguments, @Cached MemberNodes.IsMemberExisting isMemberExisting) {
        return isMemberExisting.execute(arguments[0], arguments[1]);
    }

    @Fallback
    protected Object doThrow(Object[] arguments) {
        throw SchemeException.arityException(this, IS_MEMBER_EXISTING, 2, arguments.length);
    }
}
