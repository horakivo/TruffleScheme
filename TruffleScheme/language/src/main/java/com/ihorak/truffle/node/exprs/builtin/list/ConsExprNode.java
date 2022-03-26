package com.ihorak.truffle.node.exprs.builtin.list;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemePair;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;


@NodeChild(value = "car")
@NodeChild(value = "cdr")
public abstract class ConsExprNode extends SchemeExpression {


    @Specialization
    protected SchemeCell doSchemeList(Object car, SchemeCell list) {
        return list.cons(car, list);
    }

    @Specialization
    public SchemePair doSchemePair(Object car, Object cdr) {
        return new SchemePair(car, cdr);
    }
}
