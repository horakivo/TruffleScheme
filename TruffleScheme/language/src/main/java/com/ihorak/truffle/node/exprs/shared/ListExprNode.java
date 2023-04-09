package com.ihorak.truffle.node.exprs.shared;

import com.ihorak.truffle.node.exprs.ArbitraryBuiltin;
import com.ihorak.truffle.type.SchemeList;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class ListExprNode extends ArbitraryBuiltin {

    @Specialization
    protected static SchemeList createList(Object[] arguments, @Cached ListNode listNode) {
        return listNode.execute(arguments);
    }
}
