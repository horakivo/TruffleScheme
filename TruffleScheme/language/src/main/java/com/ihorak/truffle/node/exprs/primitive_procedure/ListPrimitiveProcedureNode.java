package com.ihorak.truffle.node.exprs.primitive_procedure;

import com.ihorak.truffle.node.exprs.ArbitraryBuiltinExpression;
import com.ihorak.truffle.type.SchemeCell;
import com.oracle.truffle.api.dsl.Specialization;

import static com.ihorak.truffle.type.SchemeCell.EMPTY_LIST;

public abstract class ListPrimitiveProcedureNode extends ArbitraryBuiltinExpression {

    @Specialization(guards = "arguments.length == 0")
    protected SchemeCell createEmptyList(Object[] arguments) {
        return SchemeCell.EMPTY_LIST;
    }

    @Specialization
    protected SchemeCell createList(Object[] arguments) {
        SchemeCell list = EMPTY_LIST;
        for (int i = arguments.length; i-- > 0; ) {
            list = new SchemeCell(arguments[i], list);
        }

        return list;
    }
}
