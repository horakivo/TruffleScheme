package com.ihorak.truffle.node.exprs.builtin.list;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.shared.ListNode;
import com.ihorak.truffle.node.polyglot.TranslateInteropExceptionNode;
import com.ihorak.truffle.type.SchemeList;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.library.CachedLibrary;


@NodeChild(value = "left")
@NodeChild(value = "right")
public abstract class AppendExprNode1 extends SchemeExpression {


    @Specialization(guards = "!left.isEmpty")
    protected SchemeList bothNotEmpty(SchemeList left, SchemeList right) {
        var head = new SchemeList(left.car, null, left.size + right.size, false);
        var tail = head;
        var currentList = left.cdr;
        while (!currentList.isEmpty) {
            var cell = new SchemeList(currentList.car, null, tail.size - 1, false);
            tail.cdr = cell;
            tail = cell;
            currentList = currentList.cdr;
        }

        tail.cdr = right;


        return head;
    }

    @Specialization(guards = "left.isEmpty")
    protected SchemeList doLeftEmpty(SchemeList left, SchemeList right) {
        return right;
    }

    @Specialization(guards = {"interopLeft.hasArrayElements(left)", " interopRight.hasArrayElements(right)"}, limit = " getInteropCacheLimit()")
    protected Object doInterop(Object left,
                               Object right,
                               @Cached ListNode listNode,
                               @Cached TranslateInteropExceptionNode translateInteropExceptionNode,
                               @CachedLibrary("left") InteropLibrary interopLeft,
                               @CachedLibrary("right") InteropLibrary interopRight) {
        var leftSize = getInteropArraySize(left, interopLeft, translateInteropExceptionNode);
        var rightSize = getInteropArraySize(right, interopRight, translateInteropExceptionNode);
        var array = new Object[leftSize + rightSize];

        try {
            for (int i = 0; i < leftSize; i++) {
                array[i] = interopLeft.readArrayElement(left, i);
            }
        } catch (InteropException e) {
            throw translateInteropExceptionNode.execute(e, left, "append", null);
        }


        try {
            for (int i = 0; i < rightSize; i++) {
                array[i + leftSize] = interopRight.readArrayElement(right, i);
            }
        } catch (InteropException e) {
            throw translateInteropExceptionNode.execute(e, right, "append", null);
        }


        return listNode.execute(array);
    }



    private int getInteropArraySize(Object receiver, InteropLibrary interopLibrary, TranslateInteropExceptionNode
            translateInteropExceptionNode) {
        try {
            return (int) interopLibrary.getArraySize(receiver);
        } catch (UnsupportedMessageException e) {
            throw translateInteropExceptionNode.execute(e, receiver, "append", null);
        }
    }


}
