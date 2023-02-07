package com.ihorak.truffle.convertor.SpecialForms;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.context.LexicalScope;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.util.CreateWriteExprNode;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;

import java.util.List;

public class DefineConverter {

    private DefineConverter() {}

    public static SchemeExpression convert(SchemeList defineList, ParsingContext context, boolean isDefinitionAllowed) {
        validate(defineList, isDefinitionAllowed);

        var identifier = (SchemeSymbol) defineList.get(1);
        var defineBody = defineList.get(2);
        var isNonGlobalEnv = context.getLexicalScope() != LexicalScope.GLOBAL;
        var isFunctionDefinition = isFunctionDefinition(defineBody);


        if (isNonGlobalEnv) {
            context.findOrAddLocalSymbol(identifier);
            context.makeLocalVariablesNullable(List.of(identifier));
        }


//        var previousFunctionDefinition = context.isFunctionDefinition();
//        var previousFunctionDefinitionName = context.getFunctionDefinitionName();

        if (isFunctionDefinition) {
           // context.setFunctionDefinition(true);
            context.setFunctionDefinitionName(identifier);
        }
        var bodyExpr = InternalRepresentationConverter.convert(defineBody, context, false, false);

        context.setFunctionDefinitionName(null);

//        if (isFunctionDefinition) {
//            context.setFunctionDefinition(previousFunctionDefinition);
//            context.setFunctionDefinitionName(previousFunctionDefinitionName);
//
//        }

        if (isNonGlobalEnv) {
            context.makeLocalVariablesNonNullable(List.of(identifier));
        }

        if (context.getLexicalScope() == LexicalScope.GLOBAL) {
            return CreateWriteExprNode.createWriteGlobalVariableExprNode(identifier, bodyExpr);
        } else {
            return CreateWriteExprNode.createWriteLocalVariableExprNode(identifier, bodyExpr, context);
        }
    }


    private static boolean isFunctionDefinition(Object defineBody) {
        if (defineBody instanceof SchemeList schemeCellBody) {
            var firstElement = schemeCellBody.car();
            return (firstElement instanceof SchemeSymbol symbol && symbol.getValue().equals("lambda"));
        }

        return false;
    }

    private static void validate(SchemeList defineList, boolean isDefinitionAllowed) {
        if (!isDefinitionAllowed) throw new SchemeException("define: not allowed in an expression context", null);
        var identifier = defineList.get(1);
        var body = defineList.cdr().cdr();

        if (!(identifier instanceof SchemeSymbol)) {
            throw new SchemeException("define: bad syntax. Not an identifier. Given: " + identifier, null);
        }

        if (body.size != 1) {
            throw new SchemeException("define: multiple expressions after identifier", null);
        }
    }
}
