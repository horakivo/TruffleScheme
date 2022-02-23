package com.ihorak.truffle.node.exprs.builtin.list;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeCell;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;


//TODO when to use DSL or when just normal class
@NodeChild(value = "car")
@NodeChild(value = "cdr")
public abstract class ConsExprNode extends SchemeExpression {

    @Specialization
    public SchemeCell doCons(Object car, Object cdr) {
        return new SchemeCell(car, cdr);
    }
}
