package com.ihorak.truffle.node.builtin.core;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.builtin.list.ListBuiltinNode;
import com.ihorak.truffle.node.callable.DispatchNode;
import com.ihorak.truffle.node.callable.DispatchPrimitiveProcedureNode;
import com.ihorak.truffle.node.builtin.polyglot.TranslateInteropExceptionNode;
import com.ihorak.truffle.runtime.PrimitiveProcedure;
import com.ihorak.truffle.runtime.SchemeList;
import com.ihorak.truffle.runtime.UserDefinedProcedure;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.profiles.IntValueProfile;

public abstract class MapCoreArbitraryArgsNode extends SchemeNode {

    public abstract Object execute(Object procedure, Object arguments);


    @Specialization
    protected SchemeList doPrimitiveProcedureWithSchemeLists(PrimitiveProcedure procedure, SchemeList[] arguments,
                                                             @Cached IntValueProfile numberOfListsProfile,
                                                             @Cached IntValueProfile numberOfElementsInEachListProfile,
                                                             @Cached ListBuiltinNode listBuiltinNode,
                                                             @Cached DispatchPrimitiveProcedureNode dispatchNode) {
        int numberOfLists = numberOfListsProfile.profile(arguments.length);
        haveAllListsSameSize(arguments, numberOfLists);
        // all lists have to have same amount of elements
        int numberOfElementsInEachList = numberOfElementsInEachListProfile.profile(arguments[0].size);

        Object[] args = getArgumentsForEachCallPrimitiveProcedure(arguments, numberOfLists, numberOfElementsInEachList);

        var result = new Object[numberOfElementsInEachList];
        for (int i = 0; i < numberOfElementsInEachList; i++) {
            var argsForCall = (Object[]) args[i];
            result[i] = dispatchNode.execute(procedure, argsForCall);
        }

        return listBuiltinNode.execute(result);
    }

    @Specialization
    protected SchemeList doUserDefinedProcedureWithSchemeLists(UserDefinedProcedure procedure, SchemeList[] arguments,
                                                               @Cached IntValueProfile numberOfListsProfile,
                                                               @Cached IntValueProfile numberOfElementsInEachListProfile,
                                                               @Cached ListBuiltinNode listBuiltinNode,
                                                               @Cached DispatchNode dispatchNode) {
        int numberOfLists = numberOfListsProfile.profile(arguments.length);
        haveAllListsSameSize(arguments, numberOfLists);
        // all lists have to have same amount of elements
        int numberOfElementsInEachList = numberOfElementsInEachListProfile.profile(arguments[0].size);

        Object[] args = getArgumentsForEachCallUserDefinedProcedure(arguments, numberOfLists, numberOfElementsInEachList, procedure);

        var result = new Object[numberOfElementsInEachList];
        for (int i = 0; i < numberOfElementsInEachList; i++) {
            var argsForCall = (Object[]) args[i];
            result[i] = dispatchNode.executeDispatch(procedure, argsForCall);
        }

        return listBuiltinNode.execute(result);
    }


    @Specialization(guards = {"interopProcedure.isExecutable(procedure)", "areArgumentsForeignArrays(arguments, interop)"}, limit = "getInteropCacheLimit()")
    protected SchemeList doMapWithForeignObjects(Object procedure, Object[] arguments,
                                                 @CachedLibrary(limit = "2") InteropLibrary interop,
                                                 @CachedLibrary("procedure") InteropLibrary interopProcedure,
                                                 @Cached TranslateInteropExceptionNode translateInteropExceptionNode,
                                                 @Cached IntValueProfile numberOfListsProfile,
                                                 @Cached IntValueProfile numberOfElementsInEachListProfile,
                                                 @Cached ListBuiltinNode listBuiltinNode) {
        int numberOfLists = numberOfListsProfile.profile(arguments.length);
        haveAllListsSameSize(arguments, numberOfLists, interop, translateInteropExceptionNode);
        // all lists have to have same amount of elements
        int numberOfElementsInEachList = numberOfElementsInEachListProfile.profile(getForeignArraySize(arguments[0], interop, translateInteropExceptionNode));

        Object[] args = getArgumentsForEachCall(arguments, numberOfLists, numberOfElementsInEachList, interop, translateInteropExceptionNode);

        var result = new Object[numberOfElementsInEachList];
        for (int i = 0; i < numberOfElementsInEachList; i++) {
            var argsForCall = (Object[]) args[i];
            result[i] = executeForeignProcedure(procedure, argsForCall, interopProcedure, translateInteropExceptionNode);
        }

        return listBuiltinNode.execute(result);
    }

    @Fallback
    protected Object doThrow(Object procedure, Object arguments, @CachedLibrary(limit = "1") InteropLibrary interop) {
        if (!interop.isExecutable(procedure)) {
            throw SchemeException.notProcedure(procedure, this);
        }

        if (!interop.hasArrayElements(arguments)) {
            throw SchemeException.contractViolation(this, "map", "list?", arguments);
        }

        throw SchemeException.shouldNotReachHere();
    }


    private Object[] getArgumentsForEachCallPrimitiveProcedure(SchemeList[] arguments, final int numberOfLists, final int numberOfElementsInEachList) {
        Object[] result = new Object[numberOfElementsInEachList];

        for (int i = 0; i < numberOfElementsInEachList; i++) {
            Object[] argsForCall = new Object[numberOfLists];
            for (int k = 0; k < numberOfLists; k++) {
                argsForCall[k] = arguments[k].get(i);
            }
            result[i] = argsForCall;
        }

        return result;
    }

    private Object[] getArgumentsForEachCallUserDefinedProcedure(SchemeList[] arguments, final int numberOfLists,
                                                                 final int numberOfElementsInEachList,
                                                                 UserDefinedProcedure userDefinedProcedure) {
        Object[] result = new Object[numberOfElementsInEachList];

        for (int i = 0; i < numberOfElementsInEachList; i++) {
            Object[] argsForCall = new Object[numberOfLists + 1];
            argsForCall[0] = userDefinedProcedure.parentFrame();
            for (int k = 0; k < numberOfLists; k++) {
                argsForCall[k + 1] = arguments[k].get(i);
            }
            result[i] = argsForCall;
        }

        return result;
    }

    private void haveAllListsSameSize(SchemeList[] arguments, final int numberOfLists) {
        int size = arguments[0].size;
        for (int i = 1; i < numberOfLists; i++) {
            if (size != arguments[i].size) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                throw new SchemeException("""
                        map: all lists must have same size
                        first list length: %d
                        other list length: %d
                        """.formatted(size, arguments[i].size), this);
            }
        }
    }

    // interop
    private void haveAllListsSameSize(Object[] arguments, int numberOfLists, InteropLibrary interop, TranslateInteropExceptionNode translateInteropExceptionNode) {
        int size = getForeignArraySize(arguments[0], interop, translateInteropExceptionNode);
        for (int i = 1; i < numberOfLists; i++) {
            var currentSize = getForeignArraySize(arguments[i], interop, translateInteropExceptionNode);
            if (size != currentSize) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                throw new SchemeException("""
                        map: all lists must have same size
                        first list length: %d
                        other list length: %d
                        """.formatted(size, currentSize), this);
            }
        }
    }

    private Object[] getArgumentsForEachCall(Object[] arguments, int numberOfLists, int numberOfElementsInEachList, InteropLibrary interop, TranslateInteropExceptionNode translateInteropExceptionNode) {
        Object[] result = new Object[numberOfElementsInEachList];

        for (int i = 0; i < numberOfElementsInEachList; i++) {
            Object[] argsForCall = new Object[numberOfLists];
            for (int k = 0; k < numberOfLists; k++) {
                argsForCall[k] = readForeignArrayElement(arguments[k], i, interop, translateInteropExceptionNode);
            }
            result[i] = argsForCall;
        }

        return result;
    }

    protected boolean areArgumentsForeignArrays(Object[] arguments, InteropLibrary interop) {
        for (int i = 0; i < arguments.length; i++) {
            if (!interop.hasArrayElements(arguments[i])) {
                return false;
            }
        }

        return true;
    }
}
