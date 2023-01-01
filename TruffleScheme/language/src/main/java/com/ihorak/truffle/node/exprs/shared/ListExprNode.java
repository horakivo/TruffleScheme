package com.ihorak.truffle.node.exprs.shared;

import com.ihorak.truffle.node.exprs.ArbitraryBuiltin;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public abstract class ListExprNode extends ArbitraryBuiltin {


    @ExplodeLoop
    @Specialization(guards = {"!(arguments.length == 0)", "cachedLength == arguments.length"} , limit = "2")
    protected SchemeList createNonEmptyListFast(Object[] arguments, @Cached("arguments.length") int cachedLength) {
        var firstCell = new SchemeCell(arguments[0], SchemeCell.EMPTY_LIST);
        var list = new SchemeList(firstCell, firstCell, 1, false);

        for (int i = 1; i < cachedLength; i++) {
            var cell = new SchemeCell(arguments[i], list.bindingCell.cdr);
            list.bindingCell.cdr = cell;
            list.bindingCell = cell;
            list.size++;
        }

        return list;
    }

    @Specialization(guards = "!(arguments.length == 0)", replaces = "createNonEmptyListFast")
    protected SchemeList createNonEmptyListSlow(Object[] arguments) {
        var firstCell = new SchemeCell(arguments[0], SchemeCell.EMPTY_LIST);
        var list = new SchemeList(firstCell, firstCell, 1, false);

        for (int i = 1; i < arguments.length; i++) {
            var cell = new SchemeCell(arguments[i], list.bindingCell.cdr);
            list.bindingCell.cdr = cell;
            list.bindingCell = cell;
            list.size++;
        }

        return list;
    }

    @Specialization(guards = "arguments.length == 0")
    protected SchemeList createEmptyList(Object[] arguments) {
        return new SchemeList(SchemeCell.EMPTY_LIST, null, 0, true);
    }



//    @Children private final SchemeExpression[] arguments;
//
//    public ListExprNode(List<SchemeExpression> arguments) {
//        this.arguments = arguments.toArray(SchemeExpression[]::new);
//    }
//
//    public ListExprNode() {
//        this.arguments = new SchemeExpression[0];
//    }
//
//    @Override
//    public SchemeCell executeList(VirtualFrame virtualFrame) {
//        return createList(virtualFrame);
//    }
//
//    @Override
//    public Object executeGeneric(VirtualFrame virtualFrame) {
//        return createList(virtualFrame);
//    }
//
//    private SchemeCell createList(VirtualFrame virtualFrame) {
//        if (arguments.length == 0) {
//            return EMPTY_LIST;
//        }
//
//        SchemeCell list = EMPTY_LIST;
//        for (int i = arguments.length; i-- > 0; ) {
//            list = new SchemeCell(arguments[i].executeGeneric(virtualFrame), list);
//        }
//
//        return list;
//    }
}
