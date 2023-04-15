package com.ihorak.truffle.node.exprs.bbuiltin.list;

import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.ihorak.truffle.type.SchemeList;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.ExplodeLoop;

@GenerateUncached
public abstract class ListBuiltinNode extends AlwaysInlinableProcedureNode {

    public abstract SchemeList execute(Object[] arguments);

    @ExplodeLoop
    @Specialization(guards = {"!(arguments.length == 0)", "cachedLength == arguments.length"}, limit = "2")
    protected SchemeList doNonEmptyListCached(Object[] arguments,
                                                @Cached("arguments.length") int cachedLength) {

        var head = new SchemeList(arguments[0], null, cachedLength, false);
        var tail = head;

        for (int i = 1; i < cachedLength; i++) {
            var cell = new SchemeList(arguments[i], null, tail.size - 1, false);
            tail.cdr = cell;
            tail = cell;
        }

        tail.cdr = SchemeList.EMPTY_LIST;

        return head;
    }

    @Specialization(guards = "!(arguments.length == 0)", replaces = "doNonEmptyListCached")
    protected SchemeList doNonEmptyList(Object[] arguments) {
        var head = new SchemeList(arguments[0], null, arguments.length, false);
        var tail = head;

        for (int i = 1; i < arguments.length; i++) {
            var cell = new SchemeList(arguments[i], null, tail.size - 1, false);
            tail.cdr = cell;
            tail = cell;
        }

        tail.cdr = SchemeList.EMPTY_LIST;

        return head;
    }

    @Specialization(guards = "arguments.length == 0")
    protected SchemeList createEmptyList(Object[] arguments) {
        return SchemeList.EMPTY_LIST;
    }


}
