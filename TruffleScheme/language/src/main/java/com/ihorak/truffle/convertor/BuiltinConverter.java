package com.ihorak.truffle.convertor;

import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;

import java.util.List;

public class BuiltinConverter {

    /*
     *
     * --> (operand expr1...exprN)
     * */
    public static SchemeExpression createBuiltin(SchemeSymbol operand, List<SchemeExpression> convertedArguments, ParsingContext context) {

        switch (operand.getValue()) {
            case "+":
                return BuiltinFactory.createPlusBuiltin(convertedArguments);
            case "-":
                return BuiltinFactory.createMinusBuiltin(convertedArguments);
            case "/":
                return BuiltinFactory.createDivideBuiltin(convertedArguments);
            case "*":
                return BuiltinFactory.createMultipleBuiltin(convertedArguments);
            case "eval":
                return BuiltinFactory.createEvalBuiltin(convertedArguments, context);
            case "list":
                return BuiltinFactory.createListBuiltin(convertedArguments);
            case "cons":
                return BuiltinFactory.createConsBuiltin(convertedArguments);
            case "cdr":
                return BuiltinFactory.createCdrBuiltin(convertedArguments);
            case "car":
                return BuiltinFactory.createCarBuiltin(convertedArguments);
            case "length":
                return BuiltinFactory.createLengthBuiltin(convertedArguments);
            case "append":
                return BuiltinFactory.createAppendBuiltin(convertedArguments);
            case "map":
                return BuiltinFactory.createMapBuiltin(convertedArguments);
            case "<=":
                return BuiltinFactory.createLessThenOrEqual(convertedArguments);
            case "current-milliseconds":
                return BuiltinFactory.createCurrentMillisBuiltin(convertedArguments);
            case "display":
                return BuiltinFactory.createDisplayBuiltin(convertedArguments);
            case "newline":
                return BuiltinFactory.createNewlineBuiltin(convertedArguments);
            case "=":
                return BuiltinFactory.createEqual(convertedArguments);
            case "<":
                return BuiltinFactory.createLessThen(convertedArguments);
            case ">":
                return BuiltinFactory.createMoreThen(convertedArguments);
            case ">=":
                return BuiltinFactory.createMoreThenEqual(convertedArguments);
            case "loop":
                return BuiltinFactory.createLoop(convertedArguments);
            case "begin":
                return BuiltinFactory.createBegin(convertedArguments);
            case "list-ref":
                return BuiltinFactory.createListRef(convertedArguments);
            case "not":
                return BuiltinFactory.createNot(convertedArguments);
            case "null?":
                return BuiltinFactory.createIsNull(convertedArguments);
            case "modulo":
                return BuiltinFactory.createModulo(convertedArguments);
            case "cadr":
                return BuiltinFactory.createCadr(convertedArguments);
            case "infinite":
                return BuiltinFactory.createInfinite(convertedArguments);
            default:
                throw new RuntimeException("Unable to convert builtin procedure from list to AST. Builtin: " + operand);
        }

    }
}
