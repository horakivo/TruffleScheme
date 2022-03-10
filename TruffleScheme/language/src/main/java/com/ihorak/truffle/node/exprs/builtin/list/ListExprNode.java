package com.ihorak.truffle.node.exprs.builtin.list;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeCell;
import com.oracle.truffle.api.frame.VirtualFrame;

import java.util.List;

import static com.ihorak.truffle.type.SchemeCell.EMPTY_LIST;

public class ListExprNode extends SchemeExpression {

    @Children private final SchemeExpression[] arguments;

    public ListExprNode(List<SchemeExpression> arguments) {
        this.arguments = arguments.toArray(SchemeExpression[]::new);
    }

    @Override
    public SchemeCell executeList(VirtualFrame virtualFrame) {
        return createList(virtualFrame);
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        return createList(virtualFrame);
    }

    private SchemeCell createList(VirtualFrame virtualFrame) {
        if (arguments.length == 0) {
            return EMPTY_LIST;
        }

        SchemeCell list = EMPTY_LIST;
        for (int i = arguments.length; i-- > 0; ) {
            list = new SchemeCell(arguments[i].executeGeneric(virtualFrame), list);
        }

        return list;
    }
}
