package com.ihorak.truffle.convertor.PrimitiveTypes;

import com.ihorak.truffle.convertor.CallableConverter;
import com.ihorak.truffle.convertor.SchemeMacroDefinitionConverter;
import com.ihorak.truffle.convertor.SpecialFormConverter;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.util.SpecialFormUtils;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;

public class SchemeListConverter {

    private SchemeListConverter() {
    }


    public static SchemeExpression convert(SchemeList list, ParsingContext context, boolean isTailCall, boolean isDefinitionAllowed, ParserRuleContext ctx) {
        var firstElement = list.get(0);
        var ctxWithoutForm = (ParserRuleContext) ctx.getChild(0);

        if (isSpecialForm(firstElement)) {
            return SpecialFormConverter.convertListToSpecialForm(list, context, isDefinitionAllowed, ctxWithoutForm);
        } else if (isMacroDefinition(firstElement)) {
            return SchemeMacroDefinitionConverter.convertMarco(list, context, ctxWithoutForm);
        } else {
            return CallableConverter.convertListToProcedureCall(list, context, isTailCall, ctxWithoutForm);
        }
    }

    private static boolean isSpecialForm(Object firstElementOfList) {
        return firstElementOfList instanceof SchemeSymbol symbol && SpecialFormUtils.isSpecialForm(symbol);
    }

    private static boolean isMacroDefinition(Object firstElementOfList) {
        return firstElementOfList instanceof SchemeSymbol symbol && symbol.equals(new SchemeSymbol("define-macro"));
    }
}
