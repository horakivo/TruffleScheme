package com.ihorak.truffle.node.builtin.polyglot;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

import static com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes.HAS_MEMBERS;
import static com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes.IS_MEMBER_READABLE;

public abstract class IsMemberReadableBuiltinNode extends AlwaysInlinableProcedureNode {

    // TODO otestovat jaka vyjimka bude vyhozena kdyby tam poslal neco co neni identifier
    @Specialization(guards = "arguments.length == 2")
    protected boolean isMemberReadable(Object[] arguments, @Cached MemberNodes.IsMemberReadable isMemberReadable) {
        return isMemberReadable.execute(arguments[0], arguments[1]);
    }

    @Fallback
    protected Object doThrow(Object[] arguments) {
        throw SchemeException.arityException(this, IS_MEMBER_READABLE, 2, arguments.length);
    }
}
