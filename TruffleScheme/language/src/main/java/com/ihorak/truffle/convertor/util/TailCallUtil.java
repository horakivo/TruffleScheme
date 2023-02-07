package com.ihorak.truffle.convertor.util;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;

import java.util.ArrayList;
import java.util.List;

public class TailCallUtil {

    private TailCallUtil() {}



    //body is e.g. body of lambda or let where definitions+ are allowed
    //TCO: (<definitions>+ <expressions>* <tail expression>)
    public static List<SchemeExpression> convertBodyToSchemeExpressionsWithTCO(SchemeList body, ParsingContext context) {
        List<SchemeExpression> result = new ArrayList<>();
        var size = body.size;
        for (int i = 0; i < size - 1; i++) {
            result.add(InternalRepresentationConverter.convert(body.get(i), context, false, true));
        }

        //TCO: (or <expression>* <tail expression>)
        if (size > 0) {
            result.add(InternalRepresentationConverter.convert(body.get(size - 1), context, true, false));

        }

        return result;
    }


    //TCO: (or <expression>* <tail expression>)
    public static List<SchemeExpression> convertExpressionsToSchemeExpressionsWithTCO(SchemeList expressions, ParsingContext context) {
        List<SchemeExpression> result = new ArrayList<>();
        var size = expressions.size;
        for (int i = 0; i < size - 1; i++) {
            result.add(InternalRepresentationConverter.convert(expressions.get(i), context, false, false));
        }

        if (size > 0) {
            result.add(InternalRepresentationConverter.convert(expressions.get(size - 1), context, true, false));

        }

        return result;
    }
}
