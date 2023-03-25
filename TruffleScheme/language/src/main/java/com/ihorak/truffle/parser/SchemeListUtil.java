package com.ihorak.truffle.parser;

import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;

public class SchemeListUtil {

    public static SchemeList createList(Object... objects) {
        if (objects.length == 0) return SchemeList.EMPTY_LIST;

        var head = new SchemeList(objects[0], null, objects.length, false);
        var tail = head;

        for (int i = 1; i < objects.length; i++) {
            var cell = new SchemeList(objects[i], null, tail.size - 1, false);
            tail.cdr = cell;
            tail = cell;
        }

        tail.cdr = SchemeList.EMPTY_LIST;

        return head;
    }
}
