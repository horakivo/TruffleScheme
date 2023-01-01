package com.ihorak.truffle.convertor.PrimitiveTypes;

import com.ihorak.truffle.convertor.ProcedureCallConverter;
import com.ihorak.truffle.convertor.SchemeMacroConverter;
import com.ihorak.truffle.convertor.SpecialFormConverter;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.util.SpecialFormUtils;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;

public class SchemeCellConverter {

    private SchemeCellConverter() {}


    public static SchemeExpression convert(SchemeList list, ParsingContext context, boolean isTailCall) {
        var firstElement = list.get(0);

        if (isSpecialForm(firstElement)) {
            return SpecialFormConverter.convertListToSpecialForm(list, context);
        } else if (isMacro(firstElement)) {
            return SchemeMacroConverter.convertMarco(list, context);
        } else {
            return ProcedureCallConverter.convertListToProcedureCall(list, context, isTailCall);
        }
    }


    private static boolean isSpecialForm(Object firstElementOfList) {
        return firstElementOfList instanceof SchemeSymbol schemeSymbol && SpecialFormUtils.isSpecialForm(schemeSymbol);
    }

    private static boolean isMacro(Object firstElementOfList) {
        return firstElementOfList instanceof SchemeSymbol && firstElementOfList.equals(new SchemeSymbol("define-macro"));
    }


}
