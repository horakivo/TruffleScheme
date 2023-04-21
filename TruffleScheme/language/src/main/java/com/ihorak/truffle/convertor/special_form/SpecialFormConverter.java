package com.ihorak.truffle.convertor.special_form;

import com.ihorak.truffle.convertor.special_form.quasiquote.QuasiquoteConverter;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.runtime.SchemeList;
import com.ihorak.truffle.runtime.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

public class SpecialFormConverter {

    public static final String ANONYMOUS_PROCEDURE = "anonymous_procedure";

    public static SchemeExpression convertListToSpecialForm(SchemeList specialFormList, ParsingContext context, boolean isTailCallPosition, boolean isDefinitionAllowed, @Nullable ParserRuleContext ctx) {
        var operationSymbol = (SchemeSymbol) specialFormList.get(0);
        return switch (operationSymbol.getValue()) {
            case "if" -> IfConverter.convert(specialFormList, isTailCallPosition, context, ctx);
            case "define" -> DefineConverter.convert(specialFormList, context, isDefinitionAllowed, ctx);
            case "lambda" -> LambdaConverter.convert(specialFormList, context, new SchemeSymbol(ANONYMOUS_PROCEDURE), ctx);
            case "quote" -> QuoteConverter.convert(specialFormList, ctx);
            case "quasiquote" -> QuasiquoteConverter.convert(specialFormList, context, ctx);
            case "let" -> LetConverter.convert(specialFormList, context, isTailCallPosition, ctx);
            case "letrec" -> LetrecConverter.convert(specialFormList, context, isTailCallPosition, ctx);
            case "and" -> AndConverter.convert(specialFormList, isTailCallPosition, context, ctx);
            case "or" -> OrConverter.convert(specialFormList, isTailCallPosition, context, ctx);
            case "cond" -> CondConverter.convertCond(specialFormList, isTailCallPosition, context, ctx);
            default -> throw new IllegalArgumentException("Unknown special form");
        };
    }


    public static boolean isSpecialForm(SchemeSymbol symbol) {
        return switch (symbol.getValue()) {
            case "if", "lambda", "define", "quote", "quasiquote", "let", "and", "or", "cond", "letrec" -> true;
            default -> false;
        };
    }


}
