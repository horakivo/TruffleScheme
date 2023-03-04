package com.ihorak.truffle.convertor;

import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

public class BuiltinConverter {

    /*
     *
     * --> (operand expr1...exprN)
     * */
    public static SchemeExpression createBuiltin(SchemeSymbol operand, List<SchemeExpression> convertedArguments, ParsingContext context, ParserRuleContext ctx) {

        return switch (operand.getValue()) {
            case "+" -> BuiltinFactory.createPlusBuiltin(convertedArguments, ctx);
            case "-" -> BuiltinFactory.createMinusBuiltin(convertedArguments, ctx);
            case "/" -> BuiltinFactory.createDivideBuiltin(convertedArguments);
            case "*" -> BuiltinFactory.createMultipleBuiltin(convertedArguments, ctx);
            case "eval" -> BuiltinFactory.createEvalBuiltin(convertedArguments, context);
            case "list" -> BuiltinFactory.createListBuiltin(convertedArguments);
            case "cons" -> BuiltinFactory.createConsBuiltin(convertedArguments, ctx);
            case "cdr" -> BuiltinFactory.createCdrBuiltin(convertedArguments);
            case "car" -> BuiltinFactory.createCarBuiltin(convertedArguments);
            case "length" -> BuiltinFactory.createLengthBuiltin(convertedArguments);
            case "append" -> BuiltinFactory.createAppendBuiltin(convertedArguments);
            case "map" -> BuiltinFactory.createMapBuiltin(convertedArguments);
            case "<=" -> BuiltinFactory.createLessThenOrEqual(convertedArguments);
            case "current-milliseconds" -> BuiltinFactory.createCurrentMillisBuiltin(convertedArguments, ctx);
            case "display" -> BuiltinFactory.createDisplayBuiltin(convertedArguments);
            case "newline" -> BuiltinFactory.createNewlineBuiltin(convertedArguments);
            case "=" -> BuiltinFactory.createEqualNumbers(convertedArguments, ctx);
            case "<" -> BuiltinFactory.createLessThen(convertedArguments, ctx);
            case ">" -> BuiltinFactory.createMoreThen(convertedArguments);
            case ">=" -> BuiltinFactory.createMoreThenEqual(convertedArguments);
            case "loop" -> BuiltinFactory.createLoop(convertedArguments);
            case "begin" -> BuiltinFactory.createBegin(convertedArguments);
            case "list-ref" -> BuiltinFactory.createListRef(convertedArguments);
            case "not" -> BuiltinFactory.createNot(convertedArguments);
            case "null?" -> BuiltinFactory.createIsNull(convertedArguments);
            case "modulo" -> BuiltinFactory.createModulo(convertedArguments, ctx);
            case "cadr" -> BuiltinFactory.createCadr(convertedArguments);
            case "infinite" -> BuiltinFactory.createInfinite(convertedArguments);
            case "equal?" -> BuiltinFactory.createEqual(convertedArguments);
            default ->
                    throw new RuntimeException("Unable to convert builtin procedure from list to AST. Builtin: " + operand);
        };

    }
}
