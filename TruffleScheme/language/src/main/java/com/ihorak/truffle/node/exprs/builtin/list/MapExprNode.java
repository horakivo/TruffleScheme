package com.ihorak.truffle.node.exprs.builtin.list;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.ProcedureDispatchNode;
import com.ihorak.truffle.node.ProcedureDispatchNodeGen;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeFunction;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

import java.util.ArrayList;
import java.util.List;

public class MapExprNode extends SchemeExpression {

    //TODO BIG TODO HERE! GET MORE INFO ABOUT @EXPLODE LOOP HERE!
    //TODO BIG TODO HERE! ZJISTIT JESTLI V IMPLEMENTACI MUZU POUZIVAT LISTS nebo jen ARRAYS! I recall that only arrays but not sure

    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private SchemeExpression operation;
    @Children private final SchemeExpression[] arguments;
    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private ProcedureDispatchNode dispatchNode = ProcedureDispatchNodeGen.create();

    public MapExprNode(SchemeExpression operation, List<SchemeExpression> arguments) {
        this.operation = operation;
        this.arguments = arguments.toArray(SchemeExpression[]::new);
    }

    @Override
    public SchemeCell executeList(VirtualFrame virtualFrame) {
        SchemeFunction function = getFunction(virtualFrame);
        var argumentsAsLists = getArgumentsAsLists(virtualFrame);
        var result = SchemeCell.EMPTY_LIST;

        if (areAllListsSameSize(argumentsAsLists) && isNumberOfListsSameAsNumberOfArgs(function, argumentsAsLists)) {
            // (map + '(1 2 3) '(4 5 6))
            //numberOfArgumentsInEachDispatch = number of lists, since for each dispatch one element will be taken from the list (2)
            //numberOfCalls = size of the lists, since we will call dispatch for each argument in the list (3)
            int numberOfArgumentsInEachDispatch = argumentsAsLists.size();
            int numberOfCalls = argumentsAsLists.get(0).size();
            for (int i = numberOfCalls; i-- > 0; ) {
                var arguments = getArgumentsForGivenDispatch(i, numberOfArgumentsInEachDispatch, argumentsAsLists, virtualFrame);
                result = result.cons(dispatchNode.executeDispatch(function.getCallTarget(), arguments), result);
            }
        }

        return result;
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        return executeList(virtualFrame);
    }


    private SchemeFunction getFunction(VirtualFrame virtualFrame) {
        try {
            return operation.executeFunction(virtualFrame);
        } catch (UnexpectedResultException e) {
            throw new SchemeException("map: contract violation \n expected: procedure? \n given: " + e.getResult());
        }
    }

    private List<SchemeCell> getArgumentsAsLists(VirtualFrame virtualFrame) {
        List<SchemeCell> argsAsLists = new ArrayList<>();

        for (SchemeExpression expr : arguments) {
            try {
                argsAsLists.add(expr.executeList(virtualFrame));
            } catch (UnexpectedResultException e) {
                throw new SchemeException("map: contract violation \n expected: lists? \n given: " + e.getResult());
            }
        }
        return argsAsLists;
    }

    private boolean areAllListsSameSize(List<SchemeCell> lists) {
        if (!lists.isEmpty()) {
            var expectedSize = lists.get(0).size();
            for (int i = 1; i < lists.size(); i++) {
                if (lists.get(i).size() != expectedSize) {
                    throw new SchemeException("map: all lists must have same size \n first list length: " + expectedSize + "\n other list length: " + lists.get(i).size());
                }
            }
            return true;
        } else {
            throw new SchemeException("map: arity mismatch \n expected at least 1 argument \n given: 0");
        }
    }

    private boolean isNumberOfListsSameAsNumberOfArgs(SchemeFunction function, List<SchemeCell> lists) {
        if (function.getExpectedNumberOfArgs() != null && function.getExpectedNumberOfArgs() != lists.size()) {
            throw new SchemeException("map: argument mismatch; \n the given procedure's expected number of arguments does not match the given number of lists \n expected: " + function.getExpectedNumberOfArgs() + "\n given: " + lists.size());
        }
        return true;
    }

    private Object[] getArgumentsForGivenDispatch(int dispatchIndex, int numberOfArgumentsInEachDispatch, List<SchemeCell> lists, VirtualFrame parentFrame) {
        Object[] arguments = new Object[numberOfArgumentsInEachDispatch + 1];
        arguments[0] = parentFrame;

        int index = 1;
        for (SchemeCell cell : lists) {
            arguments[index] = cell.get(dispatchIndex);
            index++;
        }

        return arguments;
    }
}
