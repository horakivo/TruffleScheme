package com.ihorak.truffle.node.exprs.builtin.list;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeCell;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;

@NodeChild(value = "list")
public abstract class CdrExprNode extends SchemeExpression {

    @Specialization
    protected Object doCdr(SchemeCell list) {
        if (list != SchemeCell.EMPTY_LIST) {
            return list.cdr;
        } else {
            throw new SchemeException("cdr: contract violation \n expected: pair? \n given: ()", this);
        }
    }

    @TruffleBoundary
    @Fallback
    protected Object fallback(Object value) {
        throw new SchemeException("cdr: contract violation\nexpected: pair?\ngiven: " + value, this);
    }
}
