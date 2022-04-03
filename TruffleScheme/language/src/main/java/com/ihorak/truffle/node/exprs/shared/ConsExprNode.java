package com.ihorak.truffle.node.exprs.shared;

import com.ihorak.truffle.node.exprs.BuiltinExpression;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemePair;
import com.oracle.truffle.api.dsl.Specialization;


public abstract class ConsExprNode extends BuiltinExpression {

    @Specialization
    protected SchemeCell doSchemeList(Object car, SchemeCell list) {
        return list.cons(car, list);
    }

    @Specialization
    public SchemePair doSchemePair(Object car, Object cdr) {
        return new SchemePair(car, cdr);
    }
}
