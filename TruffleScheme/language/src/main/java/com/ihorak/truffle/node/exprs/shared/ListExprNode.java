package com.ihorak.truffle.node.exprs.shared;

import com.ihorak.truffle.node.exprs.ArbitraryBuiltin;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public abstract class ListExprNode extends ArbitraryBuiltin {

    @Child ListNode listNode = ListNodeGen.create();

    @Specialization
    protected SchemeList createList(Object[] arguments) {

        return listNode.execute(arguments);
    }

}
