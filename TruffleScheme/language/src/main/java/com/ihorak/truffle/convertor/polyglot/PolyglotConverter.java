package com.ihorak.truffle.convertor.polyglot;

import com.ihorak.truffle.convertor.callable.AbstractCallableConverter;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.runtime.SchemeList;
import com.ihorak.truffle.runtime.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.ihorak.truffle.node.polyglot.MemberNodes.*;

public class PolyglotConverter extends AbstractCallableConverter {

    public static final String POLYGLOT_EVAL_SOURCE = "eval-source";
    public static final String POLYGLOT_READ_GLOBAL_SCOPE = "read-global-scope";


    private PolyglotConverter() {
    }

    public static SchemeExpression convert(SchemeList callableListIR, ParsingContext context, @Nullable ParserRuleContext procedureCtx) {
        var symbol = (SchemeSymbol) callableListIR.car;
        List<SchemeExpression> arguments = convertArguments(callableListIR.cdr, context, procedureCtx);
        return PolyglotConverter.createPolyglotNode(symbol, arguments, procedureCtx);
    }

    private static SchemeExpression createPolyglotNode(SchemeSymbol operand, List<SchemeExpression> convertedArguments, @Nullable ParserRuleContext ctx) {
        return switch (operand.value()) {
            case POLYGLOT_EVAL_SOURCE -> PolyglotFactory.createEvalSource(convertedArguments, ctx);
            case POLYGLOT_READ_GLOBAL_SCOPE -> PolyglotFactory.createReadForeignGlobalScope(convertedArguments, ctx);
            //case GET_MEMBERS -> PolyglotFactory.createGetMembers(convertedArguments, ctx);
//            case HAS_MEMBERS -> PolyglotFactory.createHasMembers(convertedArguments, ctx);
//            case IS_MEMBER_READABLE -> PolyglotFactory.createIsMemberReadable(convertedArguments, ctx);
//            case IS_MEMBER_MODIFIABLE -> PolyglotFactory.createIsMemberModifiable(convertedArguments, ctx);
//            case IS_MEMBER_INSERTABLE -> PolyglotFactory.createIsMemberInsertable(convertedArguments, ctx);
//            case IS_MEMBER_REMOVABLE -> PolyglotFactory.createIsMemberRemovable(convertedArguments, ctx);
//            case IS_MEMBER_INVOCABLE -> PolyglotFactory.createIsMemberInvocable(convertedArguments, ctx);
//            case IS_MEMBER_WRITABLE -> PolyglotFactory.createIsMemberWritable(convertedArguments, ctx);
//            case IS_MEMBER_EXISTING -> PolyglotFactory.createIsMemberExisting(convertedArguments, ctx);
//            case READ_MEMBER -> PolyglotFactory.createReadMember(convertedArguments, ctx);
//            case WRITE_MEMBER -> PolyglotFactory.createWriteMember(convertedArguments, ctx);
//            case REMOVE_MEMBER -> PolyglotFactory.createRemoveMember(convertedArguments, ctx);
//            case INVOKE_MEMBER -> PolyglotFactory.createInvokeMember(convertedArguments, ctx);
            default ->
                    throw new RuntimeException("Unable to convert builtin procedure from list to AST. Builtin: " + operand);
        };

    }

    public static boolean isPolyglotAPI(SchemeSymbol symbol) {
        switch (symbol.value()) {
            case POLYGLOT_EVAL_SOURCE:
            case POLYGLOT_READ_GLOBAL_SCOPE:
  //          case HAS_MEMBERS:
            //case GET_MEMBERS:
//            case IS_MEMBER_READABLE:
//            case IS_MEMBER_MODIFIABLE:
//            case IS_MEMBER_INSERTABLE:
//            case IS_MEMBER_REMOVABLE:
//            case IS_MEMBER_INVOCABLE:
//            case IS_MEMBER_WRITABLE:
//            case IS_MEMBER_EXISTING:
//            case READ_MEMBER:
//            case WRITE_MEMBER:
//            case REMOVE_MEMBER:
//            case INVOKE_MEMBER:
            case ".":
                return true;
            default:
                return false;
        }
    }
}
