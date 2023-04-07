package com.ihorak.truffle.node.exprs.shared;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.exprs.ArbitraryBuiltin;
import com.ihorak.truffle.type.AbstractProcedure;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.BranchProfile;
import com.oracle.truffle.api.profiles.ConditionProfile;

//public abstract class MapExprNode extends ArbitraryBuiltin {
//
//    private final BranchProfile notCallableProcedureProfile = BranchProfile.create();
//    private final ConditionProfile notAllListProfile = ConditionProfile.createBinaryProfile();
//    private final ConditionProfile allListsSameSizeProfile = ConditionProfile.createBinaryProfile();
//    @Child
//    private ApplyExprNode apply = ApplyExprNodeGen.create();
//
//    //composition of inheritence
//
//
////    @Specialization(guards = "cachedLength == arguments.length", limit = "2")
////    protected SchemeCell mapFast(VirtualFrame frame, Object[] arguments,
////                                 @Cached("arguments.length") int cachedLength) {
////
////        final var callable = getCallableProcedure(arguments);
////        final var lists = getListsTest(arguments, cachedLength);
////        var materializedParentFrame = frame.materialize();
////        int numberOfCalls = lists[0].size();
////
////        var resultList = SchemeCell.EMPTY_LIST;
////        for (int i = numberOfCalls - 1; i >= 0; i--) {
////            Object[] args = getArgumentsForGivenDispatch(i, lists);
////            var result = apply.execute(materializedParentFrame, callable, args);
////            resultList = resultList.cons(result, resultList);
////        }
////
////        return resultList;
////    }
//    @Specialization
//    protected SchemeCell map(VirtualFrame frame, Object[] arguments) {
//        final var callable = getCallableProcedure(arguments);
//        final var lists = getLists(arguments);
//        var materializedParentFrame = frame.materialize();
//        int numberOfCalls = lists[0].size();
//
//        var resultList = SchemeCell.EMPTY_LIST;
//        for (int i = numberOfCalls - 1; i >= 0; i--) {
//            Object[] args = getArgumentsForGivenDispatch(i, lists);
//            var result = apply.execute(materializedParentFrame, callable, args);
//            resultList = resultList.cons(result, resultList);
//        }
//
//        return resultList;
//    }
//
//
//
//
//    private Object[] getArgumentsForGivenDispatch(int dispatchIndex, SchemeCell[] arguments) {
//        Object[] result = new Object[arguments.length];
//
//        for (int i = 0; i < arguments.length; i++) {
//            result[i] = arguments[i].get(dispatchIndex);
//        }
//
//        return result;
//    }
//
//    private AbstractProcedure getCallableProcedure(Object[] arguments) {
//        if (arguments[0] instanceof AbstractProcedure) {
//            return (AbstractProcedure) arguments[0];
//        } else {
//            notCallableProcedureProfile.enter();
//            throw new SchemeException("map: contract violation\nexpected: procedure?\ngiven: " + arguments[0], this);
//        }
//    }
//
//    private SchemeCell[] getLists(Object[] arguments) {
//        var result = new SchemeCell[arguments.length - 1];
//
//        for (int i = 1; i < arguments.length; i++) {
//            var potentialList = arguments[i];
//            if (notAllListProfile.profile(potentialList instanceof SchemeCell)) {
//                result[i - 1] = (SchemeCell) potentialList;
//            } else {
//                throw new SchemeException("map: contract violation\nexpected: lists?\ngiven: " + potentialList, this);
//            }
//        }
//
//        if (allListsSameSizeProfile.profile(hasListsSameSize(result))) {
//            return result;
//        } else {
//            throw new SchemeException("map: contract violation\nall lists have to have same length", this);
//        }
//    }
//
//    private boolean hasListsSameSize(SchemeCell[] lists) {
//        int size = lists[0].size();
//
//        for (int i = 1; i < lists.length; i++) {
//            if (lists[i].size() != size) {
//                return false;
//            }
//        }
//
//        return true;
//    }
//
////
////    @ExplodeLoop
////    private SchemeCell[] getListsTest(Object[] arguments, int cacheLength) {
////        var result = new SchemeCell[cacheLength - 1];
////
////        for (int i = 1; i < cacheLength; i++) {
////            var potentialList = arguments[i];
////            if (notAllListProfile.profile(potentialList instanceof SchemeCell)) {
////                result[i - 1] = (SchemeCell) potentialList;
////            } else {
////                throw new SchemeException("map: contract violation\nexpected: lists?\ngiven: " + potentialList, this);
////            }
////        }
////
////        if (allListsSameSizeProfile.profile(hasListsSameSizeTest(result, cacheLength - 1))) {
////            return result;
////        } else {
////            throw new SchemeException("map: contract violation\nall lists have to have same length", this);
////        }
////    }
////
////    @ExplodeLoop
////    private boolean hasListsSameSizeTest(SchemeCell[] lists, int cacheLength) {
////        int size = lists[0].size();
////
////        for (int i = 1; i < cacheLength; i++) {
////            if (lists[i].size() != size) {
////                return false;
////            }
////        }
////
////        return true;
////    }
//}