package com.ihorak.truffle.node.exprs.builtin.list;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeCell;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;

@NodeChild(value = "left")
@NodeChild(value = "right")
public abstract class AppendExprNode extends SchemeExpression {


    @Specialization
    protected SchemeCell doAppend(SchemeCell left, SchemeCell right) {
        SchemeCell result = SchemeCell.EMPTY_LIST;

        for (int i = right.size() - 1; i >= 0; i--) {
            result = result.cons(right.get(i), result);
        }

        for (int i = left.size() - 1; i >= 0; i--) {
            result = result.cons(left.get(i), result);
        }

        return result;
    }


    @Specialization
    protected Object fallback(Object left, Object right) {
        throw new SchemeException("append: contract violation\nexpecting all arguments lists\ngiven left: " + left + "\ngiven right: " + right, this);
    }
}
