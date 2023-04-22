package com.ihorak.truffle.node.builtin.polyglot.members;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

import static com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes.HAS_MEMBERS;
import static com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes.IS_MEMBER_INSERTABLE;

public abstract class IsMemberInsertableBuiltinNode extends AlwaysInlinableProcedureNode {

    @Specialization(guards = "arguments.length == 2")
    protected boolean isMemberInsertable(Object[] arguments, @Cached MemberNodes.IsMemberInsertable isMemberInsertable) {
        return isMemberInsertable.execute(arguments[0], arguments[1]);
    }

    @Fallback
    protected Object doThrow(Object[] arguments) {
        throw SchemeException.arityException(this, IS_MEMBER_INSERTABLE, 2, arguments.length);
    }
}
