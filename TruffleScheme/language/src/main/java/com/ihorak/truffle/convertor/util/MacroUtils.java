package com.ihorak.truffle.convertor.util;

import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.type.SchemeSymbol;

public class MacroUtils {

    public static boolean isMacroDefined(Object symbol) {
        if (symbol instanceof SchemeSymbol) {
            var name = (SchemeSymbol) symbol;
            return ParsingContext.macros.containsKey(name);
        }

        return false;
    }
}
