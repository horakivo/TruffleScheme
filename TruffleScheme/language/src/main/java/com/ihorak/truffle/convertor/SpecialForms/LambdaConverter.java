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
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNode;
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNodeGen;
import com.ihorak.truffle.node.special_form.LambdaExprNode;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemePair;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.frame.FrameSlotKind;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class LambdaConverter {

    private LambdaConverter() {
    }

    private static final String NOT_IDENTIFIER_IN_PARAMS = "lambda: not an identifier in parameters";
    private static final int CTX_LAMBDA_PARAMS = 2;
    private static final int CTX_LAMBDA_BODY_INDEX = 3;
    private static final int CTX_PARAMS_OFFSET = 1;


    public static SchemeExpression convert(SchemeList lambdaListIR, ParsingContext context, SchemeSymbol name, ParserRuleContext lambdaCtx) {
        validate(lambdaListIR);
        ParsingContext lambdaContext = new ParsingContext(context, LexicalScope.LAMBDA, context.getSource());
        var argumentsIR = lambdaListIR.cdr().car();
        var lambdaBodyIR = lambdaListIR.cdr().cdr();
        var numberOfArguments = numberOfArguments(argumentsIR);

        var isProcedureBeingDefined = !name.getValue().equals("anonymous_procedure");
        if (isProcedureBeingDefined) {
            lambdaContext.setFunctionDefinitionName(name);
            lambdaContext.setFunctionNumberOfArgument(numberOfArguments);
        }


        addLocalVariablesToParsingContext(argumentsIR, lambdaContext);
        var bodyExprs = TailCallUtil.convertBodyToSchemeExpressionsWithTCO(lambdaBodyIR, lambdaContext, lambdaCtx, CTX_LAMBDA_BODY_INDEX);
        var writeLocalVariableExpr = createWriteLocalVariableNodes(argumentsIR, lambdaContext, lambdaCtx);
        var allExprs = Stream.concat(writeLocalVariableExpr.stream(), bodyExprs.stream()).toList();

        if (isProcedureBeingDefined && lambdaContext.isTailCallProcedureBeingDefined()) {
            context.addTailCallProcedure(name);
        }


        var callTarget = creatCallTarget(allExprs, name, lambdaContext);
        var hasOptionalArgs = argumentsIR instanceof SchemePair;
        var lambdaExpr = new LambdaExprNode(callTarget, numberOfArguments, hasOptionalArgs);
        return SourceSectionUtil.setSourceSectionAndReturnExpr(lambdaExpr, lambdaCtx);
    }


    private static RootCallTarget creatCallTarget(List<SchemeExpression> expressions, SchemeSymbol name, ParsingContext lambdaContext) {
        var frameDescriptor = lambdaContext.buildAndGetFrameDescriptor();
        var sourceSection = SourceSectionUtil.createSourceSection(expressions, lambdaContext.getSource());

        var isSelfTailCall = lambdaContext.getSelfTailRecursionArgumentsSlotIndexes().isPresent();
        SchemeRootNode rootNode;
        if (isSelfTailCall) {
            List<Integer> argumentsIndexes = lambdaContext.getSelfTailRecursionArgumentsSlotIndexes().get();
            var writeArgumentsToFrameSlots = createWriteArgumentsToFrameSlots(argumentsIndexes);
            int resultIndex = lambdaContext.getSelfTailRecursionResultIndex().orElseThrow(InterpreterException::shouldNotReachHere);
            rootNode = new SelfTailProcedureRootNode(name, lambdaContext.getLanguage(), frameDescriptor, expressions, writeArgumentsToFrameSlots, resultIndex, sourceSection);
        } else {
            rootNode = new SchemeRootNode(lambdaContext.getLanguage(), frameDescriptor, expressions, name, sourceSection);
        }
        return rootNode.getCallTarget();
    }

    private static List<WriteLocalVariableExprNode> createWriteArgumentsToFrameSlots(List<Integer> argumentsIndexes) {
        List<WriteLocalVariableExprNode> result = new ArrayList<>();
        for (int i = 0; i < argumentsIndexes.size(); i++) {
            var slotIndex = argumentsIndexes.get(i);
            var readProcArgExpr = new ReadProcedureArgExprNode(i);
            result.add(WriteLocalVariableExprNodeGen.create(slotIndex, readProcArgExpr));
        }
        return result;
    }


    /*
     * We need to add those first because we can convert lambdaBodyIR to SchemeExprs first to determine whether there is
     * Self Tail recursion or not. Without those parameters in Parsing context we wouldn't be able to convert bodyIR to SchemeExprs.
     *
     * If there is Self Tail recursion the lambda context will have getSelfTailRecursionArgumentIndex which is used in
     * method createWriteLocalVariableNodes.
     */
    private static void addLocalVariablesToParsingContext(Object parametersIR, ParsingContext context) {
        if (parametersIR instanceof SchemeList list) {
            for (Object obj : list) {
                var symbol = (SchemeSymbol) obj;
                context.findOrAddLocalSymbol(symbol);
            }
        } else if (parametersIR instanceof SchemePair pair) {
            //TODO implement
        } else {
            throw InterpreterException.shouldNotReachHere();
        }
    }

    private static List<SchemeExpression> createWriteLocalVariableNodes(Object params, ParsingContext context, ParserRuleContext lambdaCtx) {g
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

    private static int numberOfArguments(Object parametersIR) {
        if (parametersIR instanceof SchemeList list) return list.size;
        if (parametersIR instanceof SchemePair pair) return pair.size();

        throw InterpreterException.shouldNotReachHere();
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
