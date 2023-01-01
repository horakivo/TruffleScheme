package com.ihorak.truffle.node.exprs.builtin.list;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeList;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;


@NodeChild(value = "left")
@NodeChild(value = "right")
public abstract class AppendExprNode1 extends SchemeExpression {


    @Specialization(guards = {"!left.isEmpty", "!right.isEmpty"})
    protected SchemeList bothNoEmpty(SchemeList left, SchemeList right) {
        left.bindingCell.cdr = right.list;
        return new SchemeList(left.list, right.bindingCell, left.size + right.size, false);
    }

    @Specialization(guards = "left.isEmpty")
    protected SchemeList leftIsEmpty(SchemeList left, SchemeList right) {
        return right;
    }

    @Specialization(guards = "right.isEmpty")
    protected SchemeList rightIsEmpty(SchemeList left, SchemeList right) {
        return left;
    }

}
