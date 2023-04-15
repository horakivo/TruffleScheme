package com.ihorak.truffle.node.exprs.shared;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.bbuiltin.list.ListBuiltinNode;
import com.ihorak.truffle.type.SchemeList;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;

@NodeChild("arguments")
public abstract class ListExprNode extends SchemeExpression {

    @Specialization
    protected static SchemeList createList(Object[] arguments, @Cached ListBuiltinNode listNode) {
        return listNode.execute(arguments);
    }
}
