package com.ihorak.truffle.parser.parser;

import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;

public class SchemeListUtil {

    public static SchemeList createList(Object... objects) {
        if (objects.length == 0) return new SchemeList(SchemeCell.EMPTY_LIST, null, 0, true);

        var initialCell = new SchemeCell(objects[0], SchemeCell.EMPTY_LIST);
        var schemeList = new SchemeList(initialCell, initialCell, 1, false);
        for (int i = 1; i < objects.length; i++) {
            var toBeAdded = objects[i];
            var cell = new SchemeCell(toBeAdded, schemeList.bindingCell.cdr);
            schemeList.bindingCell.cdr = cell;
            schemeList.bindingCell = cell;
            schemeList.size++;
        }

        return schemeList;
    }
}
