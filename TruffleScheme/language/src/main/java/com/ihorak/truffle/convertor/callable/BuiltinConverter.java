package com.ihorak.truffle.convertor.callable;

import com.ihorak.truffle.convertor.BuiltinFactory;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.ihorak.truffle.node.polyglot.MemberNodes.*;

public class BuiltinConverter extends AbstractCallableConverter {

    public static boolean isBuiltinEnabled = true;
    public static final String POLYGLOT_EVAL_NAME = "eval-source";
    public static final String POLYGLOT_READ_GLOBAL_SCOPE = "p-read-global-scope";


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
            case "=" -> BuiltinFactory.createEqualNumbers(convertedArguments, ctx);
            case "<" -> BuiltinFactory.createLessThen(convertedArguments, ctx);
            case ">" -> BuiltinFactory.createMoreThen(convertedArguments, ctx);
            case ">=" -> BuiltinFactory.createMoreThenEqual(convertedArguments, ctx);
            case "not" -> BuiltinFactory.createNot(convertedArguments, ctx);
            case "null?" -> BuiltinFactory.createIsNull(convertedArguments, ctx);
            case "modulo" -> BuiltinFactory.createModulo(convertedArguments, ctx);
            case "cadr" -> BuiltinFactory.createCadr(convertedArguments, ctx);
            case "equal?" -> BuiltinFactory.createEqual(convertedArguments, ctx);
            case POLYGLOT_EVAL_NAME -> BuiltinFactory.createEvalSource(convertedArguments, ctx);
            case POLYGLOT_READ_GLOBAL_SCOPE -> BuiltinFactory.createReadForeignGlobalScope(convertedArguments, ctx);
            default ->
                    throw new RuntimeException("Unable to convert builtin procedure from list to AST. Builtin: " + operand);
        };

    }

    public static boolean isBuiltinProcedure(SchemeSymbol symbol) {
        if (isBuiltinEnabled) {
            switch (symbol.getValue()) {
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
                case "=":
                case "<":
                case "<=":
                case ">":
                case ">=":
                case "not":
                case "null?":
                case "modulo":
                case "cadr":
                case "equal?":
                case POLYGLOT_EVAL_NAME:
                case POLYGLOT_READ_GLOBAL_SCOPE:
                case HAS_MEMBERS:
                case GET_MEMBERS:
                case IS_MEMBER_READABLE:
                case IS_MEMBER_MODIFIABLE:
                case IS_MEMBER_INSERTABLE:
                case IS_MEMBER_REMOVABLE:
                case IS_MEMBER_INVOCABLE:
                case IS_MEMBER_WRITABLE:
                case IS_MEMBER_EXISTING:
                case READ_MEMBER:
                case WRITE_MEMBER:
                case REMOVE_MEMBER:
                case INVOKE_MEMBER:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }
}
