package com.ihorak.truffle.convertor.callable;

import com.ihorak.truffle.convertor.BuiltinFactory;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BuiltinConverter extends AbstractCallableConverter {

    private BuiltinConverter() {
    }

    public static SchemeExpression convert(SchemeList callableListIR, ParsingContext context, @Nullable ParserRuleContext procedureCtx) {
        var symbol = (SchemeSymbol) callableListIR.car;
        List<SchemeExpression> arguments = convertArguments(callableListIR.cdr, context, procedureCtx);
        return BuiltinConverter.createBuiltin(symbol, arguments, context, procedureCtx);
    }

    private static SchemeExpression createBuiltin(SchemeSymbol operand, List<SchemeExpression> convertedArguments, ParsingContext context, @Nullable ParserRuleContext ctx) {

        return switch (operand.getValue()) {
            case "+" -> BuiltinFactory.createPlusBuiltin(convertedArguments, ctx);
            case "-" -> BuiltinFactory.createMinusBuiltin(convertedArguments, ctx);
            case "/" -> BuiltinFactory.createDivideBuiltin(convertedArguments, ctx);
            case "*" -> BuiltinFactory.createMultipleBuiltin(convertedArguments, ctx);
            case "eval" -> BuiltinFactory.createEvalBuiltin(convertedArguments, context);
            case "list" -> BuiltinFactory.createListBuiltin(convertedArguments, ctx);
            case "cons" -> BuiltinFactory.createConsBuiltin(convertedArguments, ctx);
            case "cdr" -> BuiltinFactory.createCdrBuiltin(convertedArguments, ctx);
            case "car" -> BuiltinFactory.createCarBuiltin(convertedArguments, ctx);
            case "length" -> BuiltinFactory.createLengthBuiltin(convertedArguments, ctx);
            case "append" -> BuiltinFactory.createAppendBuiltin(convertedArguments, ctx);
            case "map" -> BuiltinFactory.createMapBuiltin(convertedArguments);
            case "<=" -> BuiltinFactory.createLessThenOrEqual(convertedArguments, ctx);
            case "current-milliseconds" -> BuiltinFactory.createCurrentMillisBuiltin(convertedArguments, ctx);
            case "display" -> BuiltinFactory.createDisplayBuiltin(convertedArguments, ctx);
            //case "newline" -> BuiltinFactory.createNewlineBuiltin(convertedArguments);
            case "=" -> BuiltinFactory.createEqualNumbers(convertedArguments, ctx);
            case "<" -> BuiltinFactory.createLessThen(convertedArguments, ctx);
            case ">" -> BuiltinFactory.createMoreThen(convertedArguments, ctx);
            case ">=" -> BuiltinFactory.createMoreThenEqual(convertedArguments, ctx);
            case "not" -> BuiltinFactory.createNot(convertedArguments, ctx);
            case "null?" -> BuiltinFactory.createIsNull(convertedArguments, ctx);
            case "modulo" -> BuiltinFactory.createModulo(convertedArguments, ctx);
            case "cadr" -> BuiltinFactory.createCadr(convertedArguments, ctx);
            case "infinite" -> BuiltinFactory.createInfinite(convertedArguments);
            case "equal?" -> BuiltinFactory.createEqual(convertedArguments, ctx);
            case "eval-source" -> BuiltinFactory.createEvalSource(convertedArguments, ctx);
            default ->
                    throw new RuntimeException("Unable to convert builtin procedure from list to AST. Builtin: " + operand);
        };

    }
}
