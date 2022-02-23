package com.ihorak.truffle.node.exprs.builtin.list;

import com.ihorak.truffle.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeCell;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;

@NodeChild(value = "list")
public abstract class CdrExprNode extends SchemeExpression {

    @Specialization
    public Object doCdr(SchemeCell list) {
        if (list != SchemeCell.EMPTY_LIST) {
            return list.cdr;
        } else {
            throw new SchemeException("cdr: contract violation \n expected: pair? \n given: ()");
        }
    }
}
