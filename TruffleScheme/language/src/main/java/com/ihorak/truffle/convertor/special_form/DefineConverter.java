package com.ihorak.truffle.convertor.special_form;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.context.LexicalScope;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.util.CreateWriteExprNode;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DefineConverter {

    private static final int CTX_DEFINE_BODY = 3;

    private DefineConverter() {
    }

    public static SchemeExpression convert(SchemeList defineList, ParsingContext context, boolean isDefinitionAllowed, ParserRuleContext defineCtx) {
        validate(defineList, isDefinitionAllowed);

        var identifier = (SchemeSymbol) defineList.get(1);
        var defineBody = defineList.get(2);
        var isNonGlobalEnv = context.getLexicalScope() != LexicalScope.GLOBAL;


        // TODO this is wrong. We need to detect whether we are in lambda or not. If not then the expr will be evaluated
        if (isNonGlobalEnv) {
            context.findOrAddLocalSymbol(identifier);
            context.makeLocalVariablesNullable(List.of(identifier));
        }

        var bodyExpr = convertDefineBodyToSchemeExpr(defineList, defineBody, context, identifier, defineCtx);

        if (isNonGlobalEnv) {
            context.makeLocalVariablesNonNullable(List.of(identifier));
        }

        if (context.getLexicalScope() == LexicalScope.GLOBAL) {
            return CreateWriteExprNode.createWriteGlobalVariableExprNode(identifier, bodyExpr, defineCtx);
        } else {
            return CreateWriteExprNode.createWriteLocalVariableExprNode(identifier, bodyExpr, context, defineCtx);
        }
    }


    private static SchemeExpression convertDefineBodyToSchemeExpr(SchemeList defineList, Object defineBody, ParsingContext context, SchemeSymbol identifier, @Nullable ParserRuleContext defineCtx) {
        var bodyFormCtx = defineCtx != null ? (ParserRuleContext) defineCtx.children.get(CTX_DEFINE_BODY) : null;
        if (isDefun(defineList)) {
            var lambdaCtx = bodyFormCtx != null ? (ParserRuleContext) bodyFormCtx.getChild(0) : null;
            return LambdaConverter.convert((SchemeList) defineBody, context, identifier, lambdaCtx);
        } else {
            return InternalRepresentationConverter.convert(defineBody, context, false, false, bodyFormCtx);
        }
    }

    private static void validate(SchemeList defineList, boolean isDefinitionAllowed) {
        if (!isDefinitionAllowed) throw new SchemeException("define: not allowed in an expression context", null);
        var identifier = defineList.get(1);
        var body = defineList.cdr.cdr;

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
