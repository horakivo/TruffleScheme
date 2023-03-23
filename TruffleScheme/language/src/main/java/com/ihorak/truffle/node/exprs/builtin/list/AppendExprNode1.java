package com.ihorak.truffle.node.exprs.builtin.list;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;


@NodeChild(value = "left")
@NodeChild(value = "right")
public abstract class AppendExprNode1 extends SchemeExpression {


    @Specialization(guards = "!left.isEmpty")
    protected SchemeList bothNoEmpty(SchemeList left, SchemeList right) {
        var head = new SchemeCell(left.car(), null);
        var tail = head;
        var currentList = left.cdr();
        while (!currentList.isEmpty) {
            var cell = new SchemeCell(currentList.car(), null);
            tail.cdr = cell;
            tail = cell;
            currentList = currentList.cdr();
        }

        tail.cdr = right.list;


        return new SchemeList(head, null, left.size + right.size, false);
    }

    @Specialization(guards = "left.isEmpty")
    protected SchemeList leftEmpty(SchemeList left, SchemeList right) {
        return right;
    }



}
