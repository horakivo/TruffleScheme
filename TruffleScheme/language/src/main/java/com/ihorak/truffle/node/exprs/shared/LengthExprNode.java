package com.ihorak.truffle.node.exprs.shared;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.exprs.BuiltinExpression;
import com.ihorak.truffle.type.SchemeCell;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class LengthExprNode extends BuiltinExpression {

    @Specialization
    public long doLength(SchemeCell list) {
        return list.size();
    }

    @Fallback
    protected void fallback(Object object) {
        throw new SchemeException("length: contract violation\nexpected: list?\ngiven: " + object, this);
    }


}

