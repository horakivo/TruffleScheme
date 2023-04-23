package com.ihorak.truffle.node.builtin.core.list;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.builtin.BinaryObjectOperationNode;
import com.ihorak.truffle.node.builtin.list.ListBuiltinNode;
import com.ihorak.truffle.node.builtin.polyglot.TranslateInteropExceptionNode;
import com.ihorak.truffle.runtime.SchemeList;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;


public abstract class AppendBinaryNode extends BinaryObjectOperationNode {

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

    @Specialization(guards = {"interopLeft.hasArrayElements(receiverLeft)", " interopRight.hasArrayElements(receiverRight)"}, limit = " getInteropCacheLimit()")
    protected Object doInterop(Object receiverLeft,
                               Object receiverRight,
                               @Cached ListBuiltinNode listNode,
                               @Cached TranslateInteropExceptionNode translateInteropExceptionNode,
                               @CachedLibrary("receiverLeft") InteropLibrary interopLeft,
                               @CachedLibrary("receiverRight") InteropLibrary interopRight) {
        var leftSize = getForeignArraySize(receiverLeft, interopLeft, translateInteropExceptionNode);
        var rightSize = getForeignArraySize(receiverRight, interopRight, translateInteropExceptionNode);
        var array = new Object[leftSize + rightSize];

        for (int i = 0; i < leftSize; i++) {
            array[i] = readForeignArrayElement(receiverLeft, i, interopLeft, translateInteropExceptionNode);
        }

        for (int i = 0; i < rightSize; i++) {
            array[i + leftSize] = readForeignArrayElement(receiverRight, i, interopRight, translateInteropExceptionNode);
        }

        return listNode.execute(array);
    }

    @Fallback
    protected Object doThrow(Object left, Object right) {
        throw SchemeException.contractViolation(this, "append", "list?", left, right);
    }
}
