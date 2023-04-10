package com.ihorak.truffle;

import com.ihorak.truffle.node.exprs.shared.ListNodeGen;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;
import org.junit.Test;

import static org.junit.Assert.*;

public class SchemeListTest {

    @Test
    public void givenSimpleList_whenCopied_correctResultIsReturned() {
        Object[] array = new Object[]{1, 2, new SchemeSymbol("ivo"), "horak"};
        var original = ListNodeGen.getUncached().execute(array);

        var clone = original.shallowClone();

        assertNotSame(clone, original);
        assertEquals(original.toString(), clone.toString());
    }

    @Test
    public void givenNestedList_whenCopied_correctResultIsReturned() {
        var sublist = new SchemeList(10L, new SchemeList(11, SchemeList.EMPTY_LIST, 1, false), 2, false);
        var original = new SchemeList(1L, new SchemeList(sublist, SchemeList.EMPTY_LIST, 1, false), 2, false);

        var clone = original.shallowClone();

        assertNotSame(clone, original);
        assertNotSame(original.get(1), clone.get(1));
        assertEquals(original.toString(), clone.toString());
    }
}
