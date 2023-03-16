package com.ihorak.truffle.convertor;

import com.ihorak.truffle.convertor.SpecialForms.*;
import com.ihorak.truffle.convertor.SpecialForms.quasiquote.QuasiquoteConverter;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;

public class SpecialFormConverter {

    public static SchemeExpression convertListToSpecialForm(SchemeList specialFormList, ParsingContext context, boolean isDefinitionAllowed, ParserRuleContext ctx) {
        var operationSymbol = (SchemeSymbol) specialFormList.get(0);
        return switch (operationSymbol.getValue()) {
            case "if" -> IfConverter.convert(specialFormList, context, ctx);
            case "define" -> DefineConverter.convert(specialFormList, context, isDefinitionAllowed, ctx);
            case "lambda" ->
                    LambdaConverter.convert(specialFormList, context, new SchemeSymbol("anonymous_procedure"), ctx);
            case "quote" -> QuoteConverter.convert(specialFormList, ctx);
            case "quasiquote" -> QuasiquoteConverter.convert(specialFormList, context, ctx);
            case "let" -> LetConverter.convert(specialFormList, context, ctx);
//            case "let*":
//                return convertLetStar(specialFormList, context);
            case "letrec" -> LetrecConverter.convert(specialFormList, context, ctx);
            case "and" -> AndConverter.convert(specialFormList, context, ctx);
            case "or" -> OrConverter.convert(specialFormList, context, ctx);
            case "cond" -> CondConverter.convertCond(specialFormList, context, ctx);
            default -> throw new IllegalArgumentException("Unknown special form");
        };
    }


}
