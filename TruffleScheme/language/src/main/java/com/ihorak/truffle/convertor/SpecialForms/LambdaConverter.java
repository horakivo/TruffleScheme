package com.ihorak.truffle.convertor.SpecialForms;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.LexicalScope;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.util.CreateWriteExprNode;
import com.ihorak.truffle.convertor.util.TailCallUtil;
import com.ihorak.truffle.exceptions.InterpreterException;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.SchemeRootNode;
import com.ihorak.truffle.node.callable.TCO.SelfTailProcedureRootNode;
import com.ihorak.truffle.node.scope.ReadProcedureArgExprNode;
import com.ihorak.truffle.node.scope.ReadSlotProcedureArgExprNode;
import com.ihorak.truffle.node.special_form.LambdaExprNode;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemePair;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.frame.FrameSlotKind;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;

public class LambdaConverter {

    private LambdaConverter() {
    }

    private static final String NOT_IDENTIFIER_IN_PARAMS = "lambda: not an identifier in parameters";
    private static final int CTX_LAMBDA_PARAMS = 2;
    private static final int CTX_LAMBDA_BODY_INDEX = 3;
    private static final int CTX_PARAMS_OFFSET = 1;


    public static LambdaExprNode convert(SchemeList lambdaList, ParsingContext context, SchemeSymbol name, ParserRuleContext lambdaCtx) {
        validate(lambdaList);
        ParsingContext lambdaContext = new ParsingContext(context, LexicalScope.LAMBDA, context.getSource());
        if (!name.getValue().equals("anonymous_procedure")) {
            lambdaContext.setFunctionDefinitionName(name);
        }

        var params = lambdaList.cdr().car();
        var expressions = lambdaList.cdr().cdr();

        addLocalVariablesToParsingContext(params, lambdaContext);
        var bodyExpressions = TailCallUtil.convertBodyToSchemeExpressionsWithTCO(expressions, lambdaContext, lambdaCtx, CTX_LAMBDA_BODY_INDEX);


        var writeLocalVariableExpr = createLocalVariableExpressions(params, lambdaContext, lambdaCtx);


        List<SchemeExpression> allExpr = new ArrayList<>();
        allExpr.addAll(writeLocalVariableExpr);
        allExpr.addAll(bodyExpressions);


        //TODO maybe create SeltTailProcedureRootNode only when we detect there is a self-tail call? Using the lambdaContext.getSelfTailRecursionArgumentInde
        int argumentsIndex = lambdaContext.getSelfTailRecursionArgumentIndex()
                .orElseGet(() -> lambdaContext.getFrameDescriptorBuilder().addSlot(FrameSlotKind.Object, null, null));
        var frameDescriptor = lambdaContext.buildAndGetFrameDescriptor();
        var sourceSection = SourceSectionUtil.createSourceSection(allExpr, lambdaContext.getSource());
        var rootNode = new SelfTailProcedureRootNode(name, context.getLanguage(), frameDescriptor, allExpr, argumentsIndex, sourceSection);
        //var rootNode = new SchemeRootNode(context.getLanguage(), frameDescriptor, allExpr, name);
        var hasOptionalArgs = params instanceof SchemePair;
        return new LambdaExprNode(rootNode.getCallTarget(), writeLocalVariableExpr.size(), hasOptionalArgs);
    }

    private static void addLocalVariablesToParsingContext(Object params, ParsingContext context) {
        if (params instanceof SchemeList list) {
            for (Object obj : list) {
                var symbol = (SchemeSymbol) obj;
                context.findOrAddLocalSymbol(symbol);
            }
        } else if (params instanceof SchemePair pair) {
            //TODO implement
        } else {
            throw InterpreterException.shouldNotReachHere();
        }
    }

    private static List<SchemeExpression> createLocalVariableExpressions(Object params, ParsingContext context, ParserRuleContext lambdaCtx) {
        var paramsCtx = (ParserRuleContext) lambdaCtx.getChild(CTX_LAMBDA_PARAMS).getChild(0);
        if (params instanceof SchemeList list) {
            return createLocalVariableForSchemeList(list, context, paramsCtx);
        } else if (params instanceof SchemePair pair) {
            return createLocalVariableForSchemePair(pair, context, paramsCtx);
        }
        throw InterpreterException.shouldNotReachHere();
    }

    private static List<SchemeExpression> createLocalVariableForSchemeList(SchemeList list, ParsingContext context, ParserRuleContext paramsCtx) {
        List<SchemeExpression> result = new ArrayList<>();
        var isSelfTailCall = context.getSelfTailRecursionArgumentIndex().isPresent();
        if (isSelfTailCall) {
            int argumentSlotIndex = context.getSelfTailRecursionArgumentIndex().get();
            for (int i = 0; i < list.size; i++) {
                var symbol = (SchemeSymbol) list.get(i);
                var symbolCtx = (ParserRuleContext) paramsCtx.getChild(i + CTX_PARAMS_OFFSET);
                result.add(CreateWriteExprNode.createWriteLocalVariableExprNode(symbol, new ReadSlotProcedureArgExprNode(i, argumentSlotIndex), context, symbolCtx));
            }
        } else {
            for (int i = 0; i < list.size; i++) {
                var symbol = (SchemeSymbol) list.get(i);
                var symbolCtx = (ParserRuleContext) paramsCtx.getChild(i + CTX_PARAMS_OFFSET);
                result.add(CreateWriteExprNode.createWriteLocalVariableExprNode(symbol, new ReadProcedureArgExprNode(i), context, symbolCtx));
            }
        }
        return result;
    }

    private static List<SchemeExpression> createLocalVariableForSchemePair(SchemePair pair, ParsingContext context, ParserRuleContext paramsCtx) {
        List<SchemeExpression> result = new ArrayList<>();
        var currentPair = pair;
        var index = 0;
        while (currentPair.second() instanceof SchemePair nextPair) {
            var symbol = (SchemeSymbol) currentPair.first();
            var symbolCtx = (ParserRuleContext) paramsCtx.getChild(index + CTX_PARAMS_OFFSET);
            result.add(CreateWriteExprNode.createWriteLocalVariableExprNode(symbol, new ReadProcedureArgExprNode(index), context, symbolCtx));
            currentPair = nextPair;
            index++;
        }
        //TODO Fix this

        return result;
    }


    // (lambda (arg1 ... argN) expr1 ..exprN)
    private static void validate(SchemeList lambdaList) {
        var params = lambdaList.cdr().car();
        var body = lambdaList.cdr().cdr();

        if (params instanceof SchemeList list) {
            for (Object obj : list) {
                if (!(obj instanceof SchemeSymbol)) {
                    throw new SchemeException(NOT_IDENTIFIER_IN_PARAMS, null);
                }
            }
        } else if (params instanceof SchemePair currentPair) {
            while (currentPair.second() instanceof SchemePair) {
                if (!(currentPair.first() instanceof SchemeSymbol)) {
                    throw new SchemeException(NOT_IDENTIFIER_IN_PARAMS, null);
                }
                currentPair = (SchemePair) currentPair.second();
            }
            if (!(currentPair.first() instanceof SchemeSymbol) || !(currentPair.second() instanceof SchemeSymbol)) {
                throw new SchemeException(NOT_IDENTIFIER_IN_PARAMS, null);
            }
        } else {
            throw new SchemeException("lambda: second element of lambda list has to be a list or pair", null);

        }

        if (body.isEmpty) {
            throw new SchemeException("lambda: no expression in body", null);
        }
    }

}
