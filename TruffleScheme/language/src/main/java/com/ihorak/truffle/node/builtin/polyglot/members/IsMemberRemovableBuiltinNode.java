package com.ihorak.truffle.node.builtin.polyglot.members;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

import static com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes.IS_MEMBER_REMOVABLE;

public abstract class IsMemberRemovableBuiltinNode extends AlwaysInlinableProcedureNode {

    @Specialization(guards = "arguments.length == 2")
    protected boolean isMemberRemovable(Object[] arguments, @Cached MemberNodes.IsMemberRemovable isMemberRemovable) {
        return isMemberRemovable.execute(arguments[0], arguments[1]);
    }

    @Fallback
    protected Object doThrow(Object[] arguments) {
        throw SchemeException.arityException(this, IS_MEMBER_REMOVABLE, 2, arguments.length);
    }
}
