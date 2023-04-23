package com.ihorak.truffle.node.polyglot;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes;
import com.ihorak.truffle.runtime.SchemeSymbol;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Executed;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.profiles.ConditionProfile;

import java.util.List;

public abstract class InvokeOrReadExprNode extends SchemeExpression {

    @Child
    @Executed
    protected SchemeExpression receiverExpr;
    @Children
    protected SchemeExpression[] arguments;
    private final SchemeSymbol identifier;
    //TODO is this good idea?
    private final Object[] emptyArgs = new Object[]{};

    public InvokeOrReadExprNode(SchemeExpression receiverExpr, List<SchemeExpression> arguments, SchemeSymbol identifier) {
        this.receiverExpr = receiverExpr;
        this.arguments = arguments.toArray(SchemeExpression[]::new);
        this.identifier = identifier;
    }

    @Specialization(guards = "arguments.length == 0")
    protected Object doInvokeOrRead(VirtualFrame frame,
                                    Object receiver,
                                    @Cached MemberNodes.InvokeMember invokeNode,
                                    @Cached MemberNodes.ReadMember readNode,
                                    @Cached ConditionProfile isInvocableProfile,
                                    @Cached MemberNodes.IsMemberInvocable isMemberInvocableNode) {
        if (isInvocableProfile.profile(isMemberInvocableNode.execute(receiver, identifier))) {
            return invokeNode.execute(receiver, identifier, emptyArgs);
        } else {
            return readNode.execute(receiver, identifier);
        }
    }

    @Specialization(guards = "arguments.length != 0")
    protected Object doInvoke(VirtualFrame frame,
                              Object receiver,
                              @Cached MemberNodes.InvokeMember invokeNode) {
        var args = getArgumentsWithoutLexicalScope(arguments, frame);
        return invokeNode.execute(receiver, identifier, args);
    }
}
