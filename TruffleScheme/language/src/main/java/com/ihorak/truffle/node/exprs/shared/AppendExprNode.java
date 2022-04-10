package com.ihorak.truffle.node.exprs.shared;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.exprs.ArbitraryBuiltin;
import com.ihorak.truffle.type.SchemeCell;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.profiles.ConditionProfile;

public abstract class AppendExprNode extends ArbitraryBuiltin {

    private final ConditionProfile notAllListProfile = ConditionProfile.createBinaryProfile();
    private final ConditionProfile oneArgNotAListProfile = ConditionProfile.createBinaryProfile();

    @Specialization(guards = "arguments.length == 0")
    protected SchemeCell noArgs(Object[] arguments) {
        return SchemeCell.EMPTY_LIST;
    }

    @Specialization(guards = "arguments.length == 1")
    protected SchemeCell oneArg(Object[] arguments) {
        var list = arguments[0];
        if (oneArgNotAListProfile.profile(list instanceof SchemeCell)) {
            return (SchemeCell) list;
        } else {
            throw new SchemeException("append: contract violation\nexpecting all arguments lists\ngiven: " + list, this);
        }
    }

    @Specialization
    protected SchemeCell doAppend(Object[] arguments) {
        var lists = getAllLists(arguments);
        SchemeCell result = SchemeCell.EMPTY_LIST;

        for (int i = lists.length - 1; i >= 0; i--) {
            var current = lists[i];
            for (int k = current.size() - 1; k >= 0; k--) {
                result = result.cons(current.get(k), result);
            }
        }

        return result;
    }

    private SchemeCell[] getAllLists(Object[] arguments) {
        SchemeCell[] lists = new SchemeCell[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            var current = arguments[i];
            if (notAllListProfile.profile(current instanceof SchemeCell)) {
                lists[i] = (SchemeCell) current;
            } else {
                throw new SchemeException("append: contract violation\nexpecting all arguments lists\ngiven: " + current, this);
            }
        }
        return lists;
    }
}
