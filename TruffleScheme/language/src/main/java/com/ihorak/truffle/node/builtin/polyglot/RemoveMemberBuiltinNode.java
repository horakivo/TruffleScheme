package com.ihorak.truffle.node.builtin.polyglot;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.ihorak.truffle.runtime.UndefinedValue;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

import static com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes.HAS_MEMBERS;
import static com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes.REMOVE_MEMBER;

public abstract class RemoveMemberBuiltinNode extends AlwaysInlinableProcedureNode {

    @Specialization(guards = "arguments.length == 2")
    protected UndefinedValue removeMember(Object[] arguments, @Cached MemberNodes.RemoveMember removeMember) {
        removeMember.execute(arguments[0], arguments[1]);
        return UndefinedValue.SINGLETON;
    }


    @Fallback
    protected Object doThrow(Object[] arguments) {
        throw SchemeException.arityException(this, REMOVE_MEMBER, 2, arguments.length);
    }
}
