package com.ihorak.truffle.convertor.util;

import com.ihorak.truffle.runtime.SchemeSymbol;

public class SpecialFormUtils {

    public static boolean isSpecialForm(SchemeSymbol symbol) {
        return switch (symbol.getValue()) {
            case "if", "lambda", "define", "quote", "quasiquote", "let", "let*", "and", "or", "cond", "letrec" -> true;
            default -> false;
        };
    }
}
