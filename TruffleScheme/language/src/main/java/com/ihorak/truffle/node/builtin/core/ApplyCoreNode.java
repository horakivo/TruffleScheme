package com.ihorak.truffle.node.builtin.core;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.callable.DispatchNode;
import com.ihorak.truffle.node.callable.DispatchPrimitiveProcedureNode;
import com.ihorak.truffle.node.polyglot.TranslateInteropExceptionNode;
import com.ihorak.truffle.runtime.PrimitiveProcedure;
import com.ihorak.truffle.runtime.SchemeList;
import com.ihorak.truffle.runtime.UserDefinedProcedure;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;

import java.beans.Transient;

public abstract class ApplyCoreNode extends SchemeNode {

    public abstract Object execute(Object procedure, Object[] optionalArguments, Object list);

    @Specialization
    protected Object doUserDefinedProcedure(UserDefinedProcedure procedure, Object[] optionalArguments, SchemeList list,
                                            @Cached DispatchNode dispatchNode) {

        var arguments = getArgumentsForUserDefinedProcedure(procedure, optionalArguments, list);
        return dispatchNode.executeDispatch(procedure, arguments);
    }

    @Specialization
    protected Object doPrimitiveProcedure(PrimitiveProcedure procedure, Object[] optionalArguments, SchemeList list,
                                          @Cached DispatchPrimitiveProcedureNode dispatchNode) {

        var arguments = getArgumentsForPrimitiveProcedure(optionalArguments, list);
        return dispatchNode.execute(procedure, arguments);
    }

    @Specialization(guards = {"interopProcedure.isExecutable(procedure)", "interopList.hasArrayElements(list)"}, limit = "getInteropCacheLimit()")
    protected Object doForeignObject(Object procedure, Object[] optionalArguments, Object list,
                                     @CachedLibrary("list") InteropLibrary interopList,
                                     @CachedLibrary("procedure") InteropLibrary interopProcedure,
                                     @Cached TranslateInteropExceptionNode translateInteropExceptionNode) {

        var arguments = getArgumentsForForeignProcedure(optionalArguments, list, interopList, translateInteropExceptionNode);
        return executeForeignProcedure(procedure, arguments, interopProcedure, translateInteropExceptionNode);
    }


    @Fallback
    protected Object doThrow(Object procedure, Object[] optionalArguments, Object list) {
        if (!InteropLibrary.getUncached().isExecutable(procedure)) {
            throw SchemeException.notProcedure(procedure, this);
        }

        if (!InteropLibrary.getUncached().hasArrayElements(list)) {
            throw SchemeException.contractViolation(this, "apply", "list?", list);
        }

        throw SchemeException.shouldNotReachHere();
    }


    private Object[] getArgumentsForPrimitiveProcedure(Object[] optionalArguments, SchemeList list) {
        Object[] result = new Object[optionalArguments.length + list.size];

        return mergeOptionalArgumentsWithSchemeList(optionalArguments, list, result, 0);
    }

    private Object[] getArgumentsForUserDefinedProcedure(UserDefinedProcedure procedure, Object[] optionalArguments, SchemeList list) {
        Object[] result = new Object[optionalArguments.length + list.size + 1];
        result[0] = procedure.getParentFrame();

        return mergeOptionalArgumentsWithSchemeList(optionalArguments, list, result, 1);
    }

    private Object[] getArgumentsForForeignProcedure(Object[] optionalArguments, Object list, InteropLibrary interopList,
                                                     TranslateInteropExceptionNode translateInteropExceptionNodeNode) {
        var listSize = getForeignArraySize(list, interopList, translateInteropExceptionNodeNode);
        Object[] result = new Object[optionalArguments.length + listSize];

        System.arraycopy(optionalArguments, 0, result, 0, optionalArguments.length);
        int index = optionalArguments.length;
        for (int i = 0; i < listSize; i++) {
            result[index] = readForeignArrayElement(list, i, interopList, translateInteropExceptionNodeNode);
            index++;
        }

        return result;
    }

    private Object[] mergeOptionalArgumentsWithSchemeList(Object[] optionalArguments, SchemeList list, Object[] result, int index) {
        for (Object obj : optionalArguments) {
            result[index] = obj;
            index++;
        }

        var currentList = list;
        while (!currentList.isEmpty) {
            result[index] = currentList.car;
            index++;
            currentList = currentList.cdr;
        }

        return result;
    }
}
