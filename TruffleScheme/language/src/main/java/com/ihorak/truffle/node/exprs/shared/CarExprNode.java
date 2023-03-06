package com.ihorak.truffle.node.exprs.shared;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.exprs.LimitedBuiltin;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemePair;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.profiles.BranchProfile;

public abstract class CarExprNode extends LimitedBuiltin {

    @Specialization
    protected Object doPairCar(SchemePair pair) {
        return pair.first();
    }

    @Specialization(guards = "!list.isEmpty")
    protected Object doSchemeList(SchemeList list) {
        return list.list.car;
    }

    @Fallback
    protected Object fallback(Object object) {
        throw SchemeException.contractViolation(this, "car", "pair? or list?", object);
    }


    public boolean isEmpty(SchemeCell list) {
        return list == SchemeCell.EMPTY_LIST;
    }
}
