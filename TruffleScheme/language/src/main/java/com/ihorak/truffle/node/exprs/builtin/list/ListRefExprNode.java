package com.ihorak.truffle.node.exprs.builtin.list;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeCell;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;

@NodeChild(value = "list")
@NodeChild(value = "index")
public abstract class ListRefExprNode extends SchemeExpression {


    @Specialization
    protected Object atIndex(SchemeCell list, long index) {
        return list.get((int) index);
    }
}
