package com.ihorak.truffle.node.builtin.polyglot;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

import static com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes.HAS_MEMBERS;
import static com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes.INVOKE_MEMBER;

public abstract class InvokeMemberBuiltinNode extends AlwaysInlinableProcedureNode {

    @Specialization(guards = "arguments.length >= 2")
    protected Object invokeMember(Object[] arguments, @Cached MemberNodes.InvokeMember invokeMember) {
        Object[] args = new Object[arguments.length - 2];
        System.arraycopy(arguments, 2, args, 0, arguments.length - 2);
        return invokeMember.execute(arguments[0], arguments[1], args);
    }

    @Fallback
    protected Object doThrow(Object[] arguments) {
        throw SchemeException.arityException(this, INVOKE_MEMBER, 3, arguments.length);
    }
}
