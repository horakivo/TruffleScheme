package com.ihorak.truffle.node.exprs.shared;

import com.ihorak.truffle.node.exprs.LimitedBuiltin;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemePair;
import com.oracle.truffle.api.dsl.Specialization;


public abstract class ConsExprNode extends LimitedBuiltin {


    @Specialization(guards = "!list.isEmpty")
    protected SchemeList doNonEmptySchemeList(Object car, SchemeList list) {
        var schemeCell = new SchemeCell(car, list.list);
        return new SchemeList(schemeCell, list.bindingCell, list.size + 1, false);
    }

    @Specialization(guards = "list.isEmpty")
    protected SchemeList doEmptySchemeList(Object car, SchemeList list) {
        var schemeCell = new SchemeCell(car, SchemeCell.EMPTY_LIST);
        return new SchemeList(schemeCell, schemeCell, 1, false);
    }

    @Specialization(guards = "!isSchemeList(cdr)")
    public SchemePair doSchemePair(Object car, Object cdr) {
        return new SchemePair(car, cdr);
    }

    public boolean isSchemeList(Object cdr) {
        return cdr instanceof SchemeList;
    }
}
