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

    private DefineConverter() {
    }

    public static SchemeExpression convert(SchemeList defineList, ParsingContext context, boolean isDefinitionAllowed) {
        validate(defineList, isDefinitionAllowed);

        if (isDefun(defineList)) {
            return convertDefun(defineList, context);
        }

        var identifier = (SchemeSymbol) defineList.get(1);
        var defineBody = defineList.get(2);
        var isNonGlobalEnv = context.getLexicalScope() != LexicalScope.GLOBAL;


        if (isNonGlobalEnv) {
            context.findOrAddLocalSymbol(identifier);
            context.makeLocalVariablesNullable(List.of(identifier));
        }


//        var previousFunctionDefinition = context.isFunctionDefinition();
//        var previousFunctionDefinitionName = context.getFunctionDefinitionName();

        var bodyExpr = InternalRepresentationConverter.convert(defineBody, context, false, false);


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


    private static SchemeExpression convertDefun(SchemeList defineList, ParsingContext context) {
        var identifier = (SchemeSymbol) defineList.get(1);
        var lambdaBody = (SchemeList) defineList.get(2);
        var isNonGlobalEnv = context.getLexicalScope() != LexicalScope.GLOBAL;

        if (isNonGlobalEnv) {
            context.findOrAddLocalSymbol(identifier);
            context.makeLocalVariablesNullable(List.of(identifier));
        }

        var lambdaExpr = LambdaConverter.convert(lambdaBody, context, identifier);


        if (isNonGlobalEnv) {
            context.makeLocalVariablesNonNullable(List.of(identifier));
        }

        if (context.getLexicalScope() == LexicalScope.GLOBAL) {
            return CreateWriteExprNode.createWriteGlobalVariableExprNode(identifier, lambdaExpr);
        } else {
            return CreateWriteExprNode.createWriteLocalVariableExprNode(identifier, lambdaExpr, context);
        }
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

    /**
     * Returns true when we are defining new function
     * list has to looks like -> (define <name> (lambda (...) ... ))
     */
    private static boolean isDefun(SchemeList defineList) {
        var body = defineList.get(2);
        if (!(body instanceof SchemeList list)) return false;

        return list.get(0) instanceof SchemeSymbol symbol && symbol.getValue().equals("lambda");
    }
}
