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


    public static List<SchemeExpression> convertBodyToSchemeExpressionsWithTCO(SchemeList body, ParsingContext context) {
        List<SchemeExpression> result = new ArrayList<>();
        var size = body.size;
        for (int i = 0; i < size - 1; i++) {
            result.add(InternalRepresentationConverter.convert(body.get(i), context, false));
        }

        //TCO: (or <expression>* <tail expression>)
        if (size > 0) {
            result.add(InternalRepresentationConverter.convert(body.get(size - 1), context, true));

        }

        return result;
    }
}
