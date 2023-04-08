package com.ihorak.truffle.convertor.special_form;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.util.CreateWriteExprNode;
import com.ihorak.truffle.convertor.util.TailCallUtil;
import com.ihorak.truffle.exceptions.InterpreterException;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.SchemeRootNode;
import com.ihorak.truffle.node.callable.TCO.SelfTailProcedureRootNode;
import com.ihorak.truffle.node.scope.ReadProcedureArgExprNode;
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNode;
import com.ihorak.truffle.node.special_form.LambdaExprNode;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemePair;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LambdaConverter {

    private LambdaConverter() {
    }

    private static final String NOT_IDENTIFIER_IN_PARAMS = "lambda: not an identifier in parameters";
    private static final int CTX_LAMBDA_PARAMS = 2;
    private static final int CTX_LAMBDA_BODY_INDEX = 3;
    private static final int CTX_PARAMS_OFFSET = 1;


    public static LambdaExprNode convert(SchemeList lambdaListIR, ParsingContext context, SchemeSymbol name, @Nullable ParserRuleContext lambdaCtx) {
        validate(lambdaListIR);
        var argumentsIR = (SchemeList) lambdaListIR.cdr.car;
        ParsingContext lambdaContext = ParsingContext.createLambdaContext(context, name, argumentsIR);
        var lambdaBodyIR = lambdaListIR.cdr.cdr;
        var numberOfArguments = numberOfArguments(argumentsIR);

        var writeLocalVariableExpr = createWriteLocalVariableNodes(argumentsIR, lambdaContext, lambdaCtx);
        var bodyExprs = TailCallUtil.convertWithDefinitionsAndWithFrameCreation(lambdaBodyIR, lambdaContext, lambdaCtx, CTX_LAMBDA_BODY_INDEX);

        var callTarget = creatCallTarget(writeLocalVariableExpr, bodyExprs, name, lambdaContext, lambdaCtx);
        var lambdaExpr = new LambdaExprNode(callTarget, numberOfArguments);
        SourceSectionUtil.setSourceSection(lambdaExpr, lambdaCtx);
        return lambdaExpr;
    }


    private static RootCallTarget creatCallTarget(List<WriteLocalVariableExprNode> writeArgsExprs, List<SchemeExpression> bodyExprs, SchemeSymbol name, ParsingContext lambdaContext, @Nullable ParserRuleContext lambdaCtx) {
        var frameDescriptor = lambdaContext.buildAndGetFrameDescriptor();
        var sourceSection = createLambdaSourceSection(lambdaContext.getSource(), lambdaCtx);

        var isSelfTailCall = lambdaContext.isFunctionSelfTailRecursive();
        SchemeRootNode rootNode;
        if (isSelfTailCall) {
            int resultIndex = lambdaContext.getSelfTCOResultFrameSlot().orElseThrow(InterpreterException::shouldNotReachHere);
            rootNode = new SelfTailProcedureRootNode(name, lambdaContext.getLanguage(), frameDescriptor, bodyExprs, writeArgsExprs, resultIndex, sourceSection);
        } else {
            var allExprs = Stream.concat(writeArgsExprs.stream(), bodyExprs.stream()).toList();
            rootNode = new SchemeRootNode(lambdaContext.getLanguage(), frameDescriptor, allExprs, name, sourceSection);
        }
        return rootNode.getCallTarget();
    }

    @Nullable
    private static SourceSection createLambdaSourceSection(Source source, @Nullable ParserRuleContext lambdaCtx) {
        if (lambdaCtx == null) return null;

        var startIndex = lambdaCtx.start.getStartIndex();
        var stopIndex = lambdaCtx.stop.getStopIndex();
        var length = stopIndex - startIndex + 1;

        return source.createSection(startIndex, length);
    }

    private static List<WriteLocalVariableExprNode> createWriteLocalVariableNodes(SchemeList argumentsIR, ParsingContext context, @Nullable ParserRuleContext lambdaCtx) {
        var paramsCtx = lambdaCtx != null ? (ParserRuleContext) lambdaCtx.getChild(CTX_LAMBDA_PARAMS).getChild(0) : null;
        return createLocalVariableForSchemeList(argumentsIR, context, paramsCtx);
//        if (params instanceof SchemeList list) {
//            return createLocalVariableForSchemeList(list, context, paramsCtx);
//        } else if (params instanceof SchemePair pair) {
//            return createLocalVariableForSchemePair(pair, context, paramsCtx);
//        }
//        throw InterpreterException.shouldNotReachHere();
    }

    private static List<WriteLocalVariableExprNode> createLocalVariableForSchemeList(SchemeList argumentListIR, ParsingContext context, @Nullable ParserRuleContext paramsCtx) {
        List<WriteLocalVariableExprNode> result = new ArrayList<>();
        for (int i = 0; i < argumentListIR.size; i++) {
            var symbol = (SchemeSymbol) argumentListIR.get(i);
            var symbolCtx = paramsCtx != null ? (ParserRuleContext) paramsCtx.getChild(i + CTX_PARAMS_OFFSET) : null;
            result.add(CreateWriteExprNode.createWriteLocalVariableExprNode(symbol, new ReadProcedureArgExprNode(i), context, symbolCtx));
        }
        return result;
    }

    private static int numberOfArguments(Object parametersIR) {
        if (parametersIR instanceof SchemeList list) return list.size;
        if (parametersIR instanceof SchemePair pair) return pair.size();

        throw InterpreterException.shouldNotReachHere();
    }

    private static List<WriteLocalVariableExprNode> createLocalVariableForSchemePair(SchemePair pair, ParsingContext context, @Nullable ParserRuleContext paramsCtx) {
        List<WriteLocalVariableExprNode> result = new ArrayList<>();
        var currentPair = pair;
        var index = 0;
        while (currentPair.second() instanceof SchemePair nextPair) {
            var symbol = (SchemeSymbol) currentPair.first();
            var symbolCtx = paramsCtx != null ? (ParserRuleContext) paramsCtx.getChild(index + CTX_PARAMS_OFFSET) : null;
            result.add(CreateWriteExprNode.createWriteLocalVariableExprNode(symbol, new ReadProcedureArgExprNode(index), context, symbolCtx));
            currentPair = nextPair;
            index++;
        }
        //TODO Fix this

        return result;
    }


    // (lambda (arg1 ... argN) expr1 ..exprN)
    private static void validate(SchemeList lambdaList) {
        var params = lambdaList.cdr.car;
        var body = lambdaList.cdr.cdr;

        if (params instanceof SchemeList list) {
            for (Object obj : list) {
                if (!(obj instanceof SchemeSymbol)) {
                    throw new SchemeException(NOT_IDENTIFIER_IN_PARAMS, null);
                }
            }
//        } else if (params instanceof SchemePair currentPair) {
//            while (currentPair.second() instanceof SchemePair) {
//                if (!(currentPair.first() instanceof SchemeSymbol)) {
//                    throw new SchemeException(NOT_IDENTIFIER_IN_PARAMS, null);
//                }
//                currentPair = (SchemePair) currentPair.second();
//            }
//            if (!(currentPair.first() instanceof SchemeSymbol) || !(currentPair.second() instanceof SchemeSymbol)) {
//                throw new SchemeException(NOT_IDENTIFIER_IN_PARAMS, null);
//            }
        } else {
            //throw new SchemeException("lambda: second element of lambda list has to be a list or pair", null);
            throw new SchemeException("lambda: second element of lambda list has to be a list", null);

        }

        if (body.isEmpty) {
            throw new SchemeException("lambda: no expression in body", null);
        }
    }

}
