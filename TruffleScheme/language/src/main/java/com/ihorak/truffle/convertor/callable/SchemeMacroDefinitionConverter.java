package com.ihorak.truffle.convertor.callable;

import com.ihorak.truffle.convertor.ConverterException;
import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.ConverterContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.literals.UndefinedLiteralNode;
import com.ihorak.truffle.runtime.SchemeList;
import com.ihorak.truffle.runtime.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

public class SchemeMacroDefinitionConverter {

    private static final int CTX_TRANSFORMATION_BODY = 3;

    private SchemeMacroDefinitionConverter() {
    }

    public static SchemeExpression convertMarco(SchemeList macroList, ConverterContext context, boolean isDefinitionAllowed, @Nullable ParserRuleContext macroCtx) {
        validate(macroList, isDefinitionAllowed);

        var name = (SchemeSymbol) macroList.get(1);
        var transformationProcedureCtx = macroCtx != null ? (ParserRuleContext) macroCtx.getChild(CTX_TRANSFORMATION_BODY) : null;
        var transformationProcedureExpr = InternalRepresentationConverter.convert(macroList.get(2), context, false, false, transformationProcedureCtx);

        context.addMacro(name, transformationProcedureExpr);
        return SourceSectionUtil.setSourceSectionAndReturnExpr(new UndefinedLiteralNode(), macroCtx);
    }


    private static void validate(SchemeList macroList, boolean isDefinitionAllowed) {
        if (!isDefinitionAllowed) throw ConverterException.definitionNotAllowed("define-macro");
        if (macroList.size != 3) {
            throw ConverterException.arityException("define-macro", 3, macroList.size);
        }

        if (!(macroList.get(1) instanceof SchemeSymbol)) {
            throw ConverterException.contractViolation("define-macro", "symbol?", macroList.get(1));
        }
    }
}
