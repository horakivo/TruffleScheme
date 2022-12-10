package com.ihorak.truffle.convertor.util;

import com.ihorak.truffle.type.SchemeSymbol;

public class BuiltinUtils {

    //For testing purpose only!
    public static boolean isBuiltinEnabled = true;

    public static boolean isBuiltinProcedure(SchemeSymbol expr) {
        if (isBuiltinEnabled) {
            switch (expr.getValue()) {
                case "+":
                case "-":
                case "/":
                case "*":
                case "eval":
                case "list":
                case "cons":
                case "cdr":
                case "car":
                case "length":
                case "append":
                case "map":
                case "current-milliseconds":
                case "display":
                case "newline":
                case "=":
                case "<":
                case "<=":
                case ">":
                case ">=":
//                case "loop":
                case "begin":
                case "list-ref":
                case "not":
                case "null?":
                case "modulo":
                case "cadr":
                case "equal?":
                case "infinite":
                    return true;
                default:
                    return false;
            }
        }

        return false;
    }
}
