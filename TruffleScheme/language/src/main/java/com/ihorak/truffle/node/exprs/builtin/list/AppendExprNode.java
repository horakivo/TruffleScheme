package com.ihorak.truffle.node.exprs.builtin.list;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.exprs.builtin.BinaryOperationNode;
import com.ihorak.truffle.type.SchemeCell;
import com.oracle.truffle.api.dsl.Specialization;

import java.util.List;

public abstract class AppendExprNode extends BinaryOperationNode {

    //TODO mozna check na list uz pri parse time? Musim zjistit jak to bude fungovat potom co budu vse reprezentovat jako SchemeCell -> tedt program jako really data
    @Specialization
    public SchemeCell doAppend(SchemeCell leftList, SchemeCell rightList) {
        if (leftList.isList() && rightList.isList()) {
            SchemeCell result = SchemeCell.EMPTY_LIST;

            List<Object> merged = leftList.convertToArrayList();
            merged.addAll(rightList.convertToArrayList());

            for (int i = merged.size(); i-- > 0; ) {
                result = result.cons(merged.get(i), result);
            }

            return result;
        } else {
            throw new SchemeException("append: contract violation\nexpecting all arguments lists", this);
        }
    }
}
