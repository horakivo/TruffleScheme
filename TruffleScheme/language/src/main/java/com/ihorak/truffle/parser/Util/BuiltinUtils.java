package com.ihorak.truffle.parser.Util;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.literals.DoubleLiteralNode;
import com.ihorak.truffle.node.literals.LongLiteralNode;
import com.ihorak.truffle.type.SchemeSymbol;

public class BuiltinUtils {

    public static boolean isBuiltinProcedure(SchemeSymbol expr) {
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
            case "<=":
            case "current-milliseconds":
            case "display":
            case "newline":
            case "=":
            case "<":
                return true;
            default:
                return false;
        }
    }
}
