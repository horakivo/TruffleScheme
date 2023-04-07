package com.ihorak.truffle.node.exprs.shared;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.exprs.LimitedBuiltin;
import com.ihorak.truffle.type.SchemeList;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class LengthExprNode extends LimitedBuiltin {

    @Specialization
    public long doLength(SchemeList list) {
        return list.size;
    }

    @Fallback
    protected void fallback(Object object) {
        throw new SchemeException("length: contract violation\nexpected: list?\ngiven: " + object, this);
    }


}

