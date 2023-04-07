package com.ihorak.truffle.node.polyglot.object;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.polyglot.PolyglotException;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.strings.TruffleString;


@NodeChild("foreignObject")
@NodeChild("identifier")
public abstract class PolyReadMemberExprNode extends SchemeExpression {


    @Specialization
    protected Object doRead(Object foreignObject, TruffleString identifier, @Cached PolyReadMemberNode readMemberNode) {
        return readMemberNode.execute(foreignObject, identifier);
    }

    @Fallback
    protected Object fallback(Object obj, Object identifier) {
        throw PolyglotException.wrongMessageIdentifierType("ReadMember", identifier, this);
    }
}
