package com.ihorak.truffle.node.builtin.core;

import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.builtin.list.ListBuiltinNode;
import com.ihorak.truffle.node.callable.DispatchNode;
import com.ihorak.truffle.node.callable.DispatchPrimitiveProcedureNode;
import com.ihorak.truffle.runtime.PrimitiveProcedure;
import com.ihorak.truffle.runtime.SchemeList;
import com.ihorak.truffle.runtime.UserDefinedProcedure;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.ExplodeLoop;


public abstract class MapCoreOneArgumentNode extends SchemeNode {

    protected abstract Object execute(Object procedure, SchemeList list);

    @ExplodeLoop
    @Specialization(guards = {"list.size == listLengthCached", "userDefinedProcedure == procedureCached"}, limit = "2")
    protected SchemeList doMapUserDefinedProcedureCached(UserDefinedProcedure userDefinedProcedure,
                                                         SchemeList list,
                                                         @Cached ListBuiltinNode listBuiltinNode,
                                                         @Cached("userDefinedProcedure") UserDefinedProcedure procedureCached,
                                                         @Cached("list.size") int listLengthCached,
                                                         @Cached DispatchNode dispatchNode) {
        Object[] resultArray = new Object[listLengthCached];
        int index = 0;
        var currentList = list;
        while (index < listLengthCached) {
            var args = new Object[]{procedureCached.parentFrame(), currentList.car};
            resultArray[index] = dispatchNode.executeDispatch(userDefinedProcedure, args);
            index++;
            currentList = currentList.cdr;
        }

        return listBuiltinNode.execute(resultArray);

    }

    @Specialization(replaces = "doMapUserDefinedProcedureCached")
    protected SchemeList doMapUserDefinedProcedureUncached(UserDefinedProcedure userDefinedProcedure,
                                                           SchemeList list,
                                                           @Cached ListBuiltinNode listBuiltinNode,
                                                           @Cached DispatchNode dispatchNode) {
        Object[] resultArray = new Object[list.size];
        int index = 0;
        var currentList = list;
        while (index < list.size) {
            var args = new Object[]{userDefinedProcedure.parentFrame(), currentList.car};
            resultArray[index] = dispatchNode.executeDispatch(userDefinedProcedure, args);
            index++;
            currentList = currentList.cdr;
        }

        return listBuiltinNode.execute(resultArray);
    }

    @ExplodeLoop
    @Specialization(guards = {"list.size == listLengthCached"}, limit = "2")
    protected SchemeList doMapPrimitiveProcedureCached(PrimitiveProcedure userDefinedProcedure,
                                                       SchemeList list,
                                                       @Cached ListBuiltinNode listBuiltinNode,
                                                       @Cached("list.size") int listLengthCached,
                                                       @Cached DispatchPrimitiveProcedureNode dispatchNode) {
        Object[] resultArray = new Object[listLengthCached];
        int index = 0;
        var currentList = list;
        while (index < listLengthCached) {
            resultArray[index] = dispatchNode.execute(userDefinedProcedure, new Object[]{currentList.car});
            index++;
            currentList = currentList.cdr;
        }

        return listBuiltinNode.execute(resultArray);
    }
}
