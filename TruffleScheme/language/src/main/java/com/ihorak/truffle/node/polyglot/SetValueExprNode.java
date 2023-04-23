package com.ihorak.truffle.node.polyglot;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes;
import com.ihorak.truffle.runtime.SchemeSymbol;
import com.ihorak.truffle.runtime.UndefinedValue;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Executed;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class SetValueExprNode extends SchemeExpression {

    @Child
    @Executed
    protected SchemeExpression receiverExpr;
    @Child
    @Executed
    protected SchemeExpression valueToStoreExpr;
    private final SchemeSymbol identifier;


    public SetValueExprNode(SchemeExpression receiverExpr, SchemeExpression valueToStoreExpr, SchemeSymbol identifier) {
        this.receiverExpr = receiverExpr;
        this.valueToStoreExpr = valueToStoreExpr;
        this.identifier = identifier;
    }


    @Specialization
    protected UndefinedValue writeMember(Object receiver, Object valueToStore,
                                         @Cached MemberNodes.IsMemberExisting isMemberExistingNode,
                                         @Cached MemberNodes.IsMemberModifiable isMemberModifiableNode,
                                         @Cached MemberNodes.WriteMember writeMemberNode) {

        if (!isMemberExistingNode.execute(receiver, identifier)) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw new SchemeException("""
                    The slot %s is missing from %s""".formatted(identifier, receiver), this);
        }

        if (!isMemberModifiableNode.execute(receiver, identifier)) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw new SchemeException("""
                    The slot %s is not modifiable""".formatted(identifier), this);
        }

        writeMemberNode.execute(receiver, identifier, valueToStore);
        return UndefinedValue.SINGLETON;
    }
}
