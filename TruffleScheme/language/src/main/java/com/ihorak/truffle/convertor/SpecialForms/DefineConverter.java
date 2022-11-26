package com.ihorak.truffle.convertor.SpecialForms;

import com.ihorak.truffle.convertor.ListToExpressionConverter;
import com.ihorak.truffle.convertor.context.LexicalScope;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.util.CreateWriteExprNode;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeSymbol;

import java.util.List;

public class DefineConverter {

    private DefineConverter() {}

    public static SchemeExpression convert(SchemeCell defineList, ParsingContext context) {
        validate(defineList);

        var identifier = (SchemeSymbol) defineList.get(1);
        var isNonGlobalEnv = context.getLexicalScope() != LexicalScope.GLOBAL;

        if (isNonGlobalEnv) {
            context.findOrAddLocalSymbol(identifier);
            context.makeLocalVariablesNullable(List.of(identifier));
        }


        var defineBody = defineList.get(2);
        var bodyExpr = ListToExpressionConverter.convert(defineBody, context);

        if (isNonGlobalEnv) {
            context.makeLocalVariablesNonNullable(List.of(identifier));
        }

        if (context.getLexicalScope() == LexicalScope.GLOBAL) {
            return CreateWriteExprNode.createWriteGlobalVariableExprNode(identifier, bodyExpr);
        } else {
            return CreateWriteExprNode.createWriteLocalVariableExprNode(identifier, bodyExpr, context);
        }
    }

    private static void validate(SchemeCell defineList) {
        var identifier = defineList.get(1);
        var body = defineList.cdr.cdr;

        if (!(identifier instanceof SchemeSymbol)) {
            throw new SchemeException("define: bad syntax. Not an identifier. Given: " + identifier, null);
        }

        if (body.size() != 1) {
            throw new SchemeException("define: multiple expressions after identifier", null);
        }
    }
}
