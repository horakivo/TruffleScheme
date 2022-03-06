package com.ihorak.truffle.node.exprs.builtin.list;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeCell;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;

@NodeChild(value = "list")
public abstract class CarExprNode extends SchemeExpression {

    @Specialization
    public Object doCar(SchemeCell list) {
        if (list != SchemeCell.EMPTY_LIST) {
            return list.car;
        } else {
            throw new SchemeException("car: contract violation\nexpected: pair?\ngiven: ()");
        }
    }

    @Fallback
    protected Object fallback(Object object) {
        throw new SchemeException("car: contract violation\n expected: pair?\n given: " + object);
    }
}
