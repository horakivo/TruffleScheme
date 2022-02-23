package com.ihorak.truffle.parser.Util;

import com.ihorak.truffle.type.SchemeSymbol;

public class SpecialFormUtils {

    public static boolean isSpecialForm(SchemeSymbol symbol) {
        switch (symbol.getValue()) {
            case "if":
            case "lambda":
            case "define":
            case "quote":
            case "quasiquote":
            case "let":
                return true;
            default:
                return false;
        }
    }
}
