package com.ihorak.truffle.node.exprs.builtin.list;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeCell;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;

@NodeChild(value = "list")
public abstract class LengthExprNode extends SchemeExpression {

    @Specialization
    public long doLength(SchemeCell list) {
        if (list.isList()) {
            return list.size();
        } else {
            throw new SchemeException("length: contract violation \n expected: list? \n given: " + list);
        }
    }


    @Fallback
    protected void fallback(Object object) {
        throw new SchemeException("length: contract violation\nexpected: list?\ngiven: " + object);
    }


}

