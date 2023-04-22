package com.ihorak.truffle.node.builtin.polyglot;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.ihorak.truffle.runtime.UndefinedValue;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

import static com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes.HAS_MEMBERS;
import static com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes.WRITE_MEMBER;

public abstract class WriteMemberBuiltinNode extends AlwaysInlinableProcedureNode {

    @Specialization(guards = "arguments.length == 3")
    protected UndefinedValue writeMember(Object[] arguments, @Cached MemberNodes.WriteMember writeMember) {
        writeMember.execute(arguments[0], arguments[1], arguments[2]);
        return UndefinedValue.SINGLETON;
    }


    @Fallback
    protected Object doThrow(Object[] arguments) {
        throw SchemeException.arityException(this, WRITE_MEMBER, 3, arguments.length);
    }
}
