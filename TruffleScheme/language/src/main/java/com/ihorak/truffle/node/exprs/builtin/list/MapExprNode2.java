package com.ihorak.truffle.node.exprs.builtin.list;

import com.ihorak.truffle.node.exprs.ArbitraryBuiltin;
import com.ihorak.truffle.type.SchemeCell;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class MapExprNode2 extends ArbitraryBuiltin {



    @Specialization
    protected SchemeCell map(Object[] arguments) {
        var callable = arguments[0];
        if (callable in)
    }
}
