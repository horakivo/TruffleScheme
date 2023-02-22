package com.ihorak.truffle.convertor.PrimitiveTypes;

import com.ihorak.truffle.convertor.ProcedureCallConverter;
import com.ihorak.truffle.convertor.SchemeMacroConverter;
import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.SpecialFormConverter;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.util.SpecialFormUtils;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;
import org.antlr.v4.runtime.Token;

import javax.xml.transform.Source;

public class SchemeListConverter {

    private SchemeListConverter() {
    }


    public static SchemeExpression convert(SchemeList list, ParsingContext context, boolean isTailCall, boolean isDefinitionAllowed) {
        var firstElement = list.get(0);

        if (isSpecialForm(firstElement)) {
            return SpecialFormConverter.convertListToSpecialForm(list, context, isDefinitionAllowed);
        } else if (isMacro(firstElement)) {
            return SchemeMacroConverter.convertMarco(list, context);
        } else {
            return ProcedureCallConverter.convertListToProcedureCall(list, context, isTailCall);
        }
    }

    public static SchemeExpression convert(SchemeList list, ParsingContext context, boolean isTailCall, boolean isDefinitionAllowed, Token startToken, Token stopToken) {
        var expr = convert(list, context, isTailCall, isDefinitionAllowed);
        SourceSectionUtil.setSourceSection(expr, startToken, stopToken);

        return expr;
    }


    private static boolean isSpecialForm(Object firstElementOfList) {
        return firstElementOfList instanceof SchemeSymbol schemeSymbol && SpecialFormUtils.isSpecialForm(schemeSymbol);
    }

    private static boolean isMacro(Object firstElementOfList) {
        return firstElementOfList instanceof SchemeSymbol && firstElementOfList.equals(new SchemeSymbol("define-macro"));
    }


}
