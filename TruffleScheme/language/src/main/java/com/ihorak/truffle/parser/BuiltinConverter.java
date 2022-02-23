package com.ihorak.truffle.parser;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;

import java.util.List;

public class BuiltinConverter {

    /*
     *
     * --> (operand expr1...exprN)
     * */
    public static SchemeExpression createBuiltin(SchemeSymbol operand, List<SchemeExpression> convertedArguments, Context context) {

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
            case "test":
                return BuiltinFactory.test(convertedArguments);
            default:
                throw new RuntimeException("Unable to convert builtin procedure from list to AST. Builtin: " + operand);
        }

    }
}
