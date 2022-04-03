package com.ihorak.truffle.node.exprs.shared;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.exprs.BuiltinExpression;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemePair;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.profiles.BranchProfile;

public abstract class CarExprNode extends BuiltinExpression {

    private final BranchProfile emptyListProfile = BranchProfile.create();

    @Specialization
    protected Object doPairCar(SchemePair pair) {
        return pair.getFirst();
    }

    @Specialization
    protected Object doSchemeList(SchemeCell list) {
        if (list == SchemeCell.EMPTY_LIST) {
            emptyListProfile.enter();
            throw new SchemeException("car: contract violation\nexpected: pair? or list?\ngiven: " + list, this);
        }
        return list.car;
    }

    @Fallback
    protected Object fallback(Object object) {
        throw new SchemeException("car: contract violation\nexpected: pair? or list?\ngiven: " + object, this);
    }
}
