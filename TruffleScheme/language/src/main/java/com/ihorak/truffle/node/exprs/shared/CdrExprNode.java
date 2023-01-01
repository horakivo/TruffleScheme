package com.ihorak.truffle.node.exprs.shared;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.exprs.LimitedBuiltin;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemePair;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.profiles.BranchProfile;

public abstract class CdrExprNode extends LimitedBuiltin {

    private final BranchProfile emptyListProfile = BranchProfile.create();


    @Specialization(guards = "!list.isEmpty")
    protected SchemeList doList(SchemeList list) {
        var size = list.size - 1;
        return new SchemeList(list.list.cdr, list.bindingCell, list.size - 1, size == 0);
    }

    @Specialization
    protected Object doPair(SchemePair pair) {
        return pair.second();
    }


    @Fallback
    protected Object fallback(Object value) {
        throw new SchemeException("cdr: contract violation\nexpected: pair? or list?\ngiven: " + value, this);
    }
}
