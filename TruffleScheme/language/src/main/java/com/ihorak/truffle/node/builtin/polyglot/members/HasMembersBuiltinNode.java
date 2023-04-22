package com.ihorak.truffle.node.builtin.polyglot.members;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

import static com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes.HAS_MEMBERS;

public abstract class HasMembersBuiltinNode extends AlwaysInlinableProcedureNode {

    @Specialization(guards = "arguments.length == 1")
    protected boolean hasMembers(Object[] arguments, @Cached MemberNodes.HasMembers hasMembers) {
        return hasMembers.execute(arguments[0]);
    }

    @Fallback
    protected Object doThrow(Object[] arguments) {
        throw SchemeException.arityException(this, HAS_MEMBERS, 1, arguments.length);
    }
}
