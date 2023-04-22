package com.ihorak.truffle.node.builtin.polyglot;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

import static com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes.GET_MEMBERS;

public abstract class GetMembersBuiltinNode extends AlwaysInlinableProcedureNode {

    @Specialization(guards = "arguments.length == 1")
    protected Object getMembers(Object[] arguments, @Cached MemberNodes.GetMembers getMembers) {
        return getMembers.execute(arguments[0]);
    }

    @Fallback
    protected Object doThrow(Object[] arguments) {
        throw SchemeException.arityException(this, GET_MEMBERS, 1, arguments.length);
    }
}
