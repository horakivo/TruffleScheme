package com.ihorak.truffle.convertor.primitive_type;

import com.ihorak.truffle.convertor.callable.CallableConverter;
import com.ihorak.truffle.convertor.callable.SchemeMacroDefinitionConverter;
import com.ihorak.truffle.convertor.special_form.SpecialFormConverter;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.util.SpecialFormUtils;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.runtime.SchemeList;
import com.ihorak.truffle.runtime.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

public class SchemeListConverter {

    private SchemeListConverter() {
    }


    public static SchemeExpression convert(SchemeList list, ParsingContext context, boolean isTailCallPosition, boolean isDefinitionAllowed, @Nullable ParserRuleContext ctx) {
        var firstElement = list.get(0);
        var ctxWithoutForm = ctx != null ? (ParserRuleContext) ctx.getChild(0) : null;

        if (isSpecialForm(firstElement)) {
            return SpecialFormConverter.convertListToSpecialForm(list, context, isTailCallPosition, isDefinitionAllowed, ctxWithoutForm);
        } else if (isMacroDefinition(firstElement)) {
            return SchemeMacroDefinitionConverter.convertMarco(list, context, isDefinitionAllowed, ctxWithoutForm);
        } else {
            return CallableConverter.convertListToProcedureCall(list, context, isTailCallPosition, ctxWithoutForm);
        }
    }

    private static boolean isSpecialForm(Object firstElementOfList) {
        return firstElementOfList instanceof SchemeSymbol symbol && SpecialFormUtils.isSpecialForm(symbol);
    }

    private static boolean isMacroDefinition(Object firstElementOfList) {
        return firstElementOfList instanceof SchemeSymbol symbol && symbol.equals(new SchemeSymbol("define-macro"));
    }
}
