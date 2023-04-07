package com.ihorak.truffle.node.exprs.builtin.comparison;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeList;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;

@NodeChild(value = "value")
public abstract class IsNullExprNode extends SchemeExpression {

    @Specialization
    protected boolean doList(SchemeList list) {
        return list.isEmpty;
    }


    @Specialization
    protected boolean doObject(Object obj) {
        return false;
    }
}
