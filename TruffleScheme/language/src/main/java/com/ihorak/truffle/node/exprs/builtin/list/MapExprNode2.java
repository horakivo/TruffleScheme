package com.ihorak.truffle.node.exprs.builtin.list;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.exprs.ArbitraryBuiltin;
import com.ihorak.truffle.type.AbstractProcedure;
import com.ihorak.truffle.type.SchemeCell;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.BranchProfile;

public abstract class MapExprNode2 extends ArbitraryBuiltin {

    private final BranchProfile notCallableProcedureProfile = BranchProfile.create();
    private final BranchProfile notAllListsProfile = BranchProfile.create();
    @Child private ApplyExprNode apply = ApplyExprNodeGen.create();


    @Specialization
    protected SchemeCell map(VirtualFrame frame, Object[] arguments) {
        final var callable = getCallableProcedure(arguments);
        final var lists = getLists(arguments);
        var materializedParentFrame = frame.materialize();
        int numberOfCalls = lists[0].size();

        var resultList = SchemeCell.EMPTY_LIST;
        for (int i = 0; i < numberOfCalls; i++) {
            Object[] args = getArgumentsForGivenDispatch(i, lists, materializedParentFrame);
            var result =  apply.execute(callable, args);
            resultList = resultList.cons(result, resultList);
        }

        return resultList;
    }


    private Object[] getArgumentsForGivenDispatch(int dispatchIndex, SchemeCell[] arguments, MaterializedFrame parentFrame) {
        Object[] result = new Object[arguments.length + 1];
        result[0] = parentFrame;

        for (int i = 0; i < arguments.length; i++) {
            result[i + 1] = arguments[i].get(dispatchIndex);
        }

        return result;
    }

    private AbstractProcedure getCallableProcedure(Object[] arguments) {
        try {
            return (AbstractProcedure) arguments[0];
        } catch (ClassCastException e) {
            notCallableProcedureProfile.enter();
            throw new SchemeException("map: contract violation \n expected: procedure? \n given: not impl yet", this);
        }
    }

    private SchemeCell[] getLists(Object[] arguments) {
        var result = new SchemeCell[arguments.length - 1];

        try {
            for (int i = 1; i < arguments.length; i++) {
                result[i - 1] = (SchemeCell) arguments[i];
            }
        } catch (ClassCastException e) {
            notAllListsProfile.enter();
            throw new SchemeException("map: contract violation \n expected: lists? \n given: not impl yet", this);
        }

        return result;
    }
}