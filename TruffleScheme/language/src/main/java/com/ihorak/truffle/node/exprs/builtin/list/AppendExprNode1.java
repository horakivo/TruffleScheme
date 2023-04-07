package com.ihorak.truffle.node.exprs.builtin.list;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeList;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;


@NodeChild(value = "left")
@NodeChild(value = "right")
public abstract class AppendExprNode1 extends SchemeExpression {


    @Specialization(guards = "!left.isEmpty")
    protected SchemeList bothNoEmpty(SchemeList left, SchemeList right) {
        var head = new SchemeList(left.car, null, left.size + right.size, false);
        var tail = head;
        var currentList = left.cdr;
        while (!currentList.isEmpty) {
            var cell = new SchemeList(currentList.car, null, tail.size - 1, false);
            tail.cdr = cell;
            tail = cell;
            currentList = currentList.cdr;
        }

        tail.cdr = right;


        return head;
    }

    @Specialization(guards = "left.isEmpty")
    protected SchemeList leftEmpty(SchemeList left, SchemeList right) {
        return right;
    }



}
