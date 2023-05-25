package com.ihorak.truffle.convertor.special_form;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.LexicalScope;
import com.ihorak.truffle.convertor.context.ConverterContext;
import com.ihorak.truffle.convertor.util.CreateWriteExprNode;
import com.ihorak.truffle.convertor.util.TailCallUtil;
import com.ihorak.truffle.exceptions.InterpreterException;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.SchemeRootNode;
import com.ihorak.truffle.node.callable.TCO.TailRecursiveRootNode;
import com.ihorak.truffle.node.callable.TCO.throwers.TailCallThrowerNode;
import com.ihorak.truffle.node.callable.TCO.throwers.TailRecursiveThrowerNode;
import com.ihorak.truffle.node.scope.ReadProcedureArgExprNode;
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNode;
import com.ihorak.truffle.node.special_form.IfElseExprNode;
import com.ihorak.truffle.node.special_form.lambda.MaterializableLambdaExprNode;
import com.ihorak.truffle.node.special_form.lambda.NonMaterializableLambdaExprNode;
import com.ihorak.truffle.runtime.SchemeList;
import com.ihorak.truffle.runtime.SchemePair;
import com.ihorak.truffle.runtime.SchemeSymbol;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.frame.FrameSlotKind;
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


    public static SchemeExpression convert(SchemeList lambdaListIR, ConverterContext context, SchemeSymbol name, @Nullable ParserRuleContext lambdaCtx) {
        validate(lambdaListIR);
        var argumentsIR = (SchemeList) lambdaListIR.cdr.car;
        ConverterContext lambdaContext = ConverterContext.createLambdaContext(context, name, argumentsIR);
        var lambdaBodyIR = lambdaListIR.cdr.cdr;

        var bodyExprs = TailCallUtil.convertWithDefinitionsAndWithFrameCreation(lambdaBodyIR, lambdaContext, lambdaCtx, CTX_LAMBDA_BODY_INDEX);
        var writeLocalVariableExpr = createWriteLocalVariableNodes(argumentsIR, lambdaContext, lambdaCtx);


        propagateClosureVariableUsageToParentFrame(lambdaContext, context);
        var callTarget = creatCallTarget(writeLocalVariableExpr, bodyExprs, name, lambdaContext, lambdaCtx);
        SchemeExpression lambdaExpr;
        if (context.isClosureVariablesUsed()) {
            lambdaExpr = new MaterializableLambdaExprNode(callTarget, argumentsIR.size, name.value());
        } else {
            lambdaExpr = new NonMaterializableLambdaExprNode(callTarget, argumentsIR.size, name.value());
        }

        SourceSectionUtil.setSourceSection(lambdaExpr, lambdaCtx);
        return lambdaExpr;
    }

    private static void propagateClosureVariableUsageToParentFrame(ConverterContext lambdaContext, ConverterContext parentContext) {
        if (lambdaContext.isClosureVariablesUsed()) {
            if (parentContext.getLexicalScope() == LexicalScope.LET || parentContext.getLexicalScope() == LexicalScope.LAMBDA) {
                parentContext.setClosureVariablesUsed(true);
            }
        }
    }

    private static RootCallTarget creatCallTarget(List<SchemeExpression> writeArgsExprs, List<SchemeExpression> bodyExprs, SchemeSymbol name, ConverterContext lambdaContext, @Nullable ParserRuleContext lambdaCtx) {
        var sourceSection = createLambdaSourceSection(lambdaContext.getSource(), lambdaCtx);

        SchemeRootNode rootNode;
        if (lambdaContext.isFunctionSelfTailRecursive()) {
            var resultIndex = lambdaContext.getFrameDescriptorBuilder().addSlot(FrameSlotKind.Object, null, null);
            var frameDescriptor = lambdaContext.getFrameDescriptorBuilder().build();
            rootNode = new TailRecursiveRootNode(name, lambdaContext.getLanguage(), frameDescriptor, bodyExprs, writeArgsExprs, resultIndex, sourceSection);
        } else {
            var allExprs = Stream.concat(writeArgsExprs.stream(), bodyExprs.stream()).toList();
            var frameDescriptor = lambdaContext.getFrameDescriptorBuilder().build();
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

    private static List<SchemeExpression> createWriteLocalVariableNodes(SchemeList argumentsIR, ConverterContext context, @Nullable ParserRuleContext lambdaCtx) {
        var paramsCtx = lambdaCtx != null ? (ParserRuleContext) lambdaCtx.getChild(CTX_LAMBDA_PARAMS).getChild(0) : null;
        return createLocalVariableForSchemeList(argumentsIR, context, paramsCtx);
    }

    private static List<SchemeExpression> createLocalVariableForSchemeList(SchemeList argumentListIR, ConverterContext context, @Nullable ParserRuleContext paramsCtx) {
        List<SchemeExpression> result = new ArrayList<>();
        for (int i = 0; i < argumentListIR.size; i++) {
            var symbol = (SchemeSymbol) argumentListIR.get(i);
            var symbolCtx = paramsCtx != null ? (ParserRuleContext) paramsCtx.getChild(i + CTX_PARAMS_OFFSET) : null;
            result.add(CreateWriteExprNode.createWriteLocalVariableExprNode(symbol, new ReadProcedureArgExprNode(i), context, symbolCtx));
        }
        return result;
    }

    private static List<WriteLocalVariableExprNode> createLocalVariableForSchemePair(SchemePair pair, ConverterContext context, @Nullable ParserRuleContext paramsCtx) {
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
