package com.ihorak.truffle.convertor.SpecialForms;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.context.LexicalScope;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.util.TailCallUtil;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.ProcedureRootNode;
import com.ihorak.truffle.node.special_form.LambdaExprNode;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemePair;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.frame.FrameSlotKind;

import java.util.ArrayList;
import java.util.List;

public class LambdaConverter {

    private LambdaConverter() {}

    private static final String NOT_IDENTIFIER_IN_PARAMS = "lambda: not an identifier in parameters";


    public static LambdaExprNode convert(SchemeCell lambdaList, ParsingContext context) {
        validate(lambdaList);
        ParsingContext lambdaContext = new ParsingContext(context, LexicalScope.LAMBDA);

        var params = lambdaList.cdr.car;
        var expressions = lambdaList.cdr.cdr;

        updateParsingContext(params, lambdaContext);
        var bodyExpressions = TailCallUtil.convertBodyToSchemeExpressionsWithTCO(expressions, lambdaContext);

        int argumentsIndex= lambdaContext.getFrameDescriptorBuilder().addSlot(FrameSlotKind.Object, null, null);
        var frameDescriptor = lambdaContext.buildAndGetFrameDescriptor();
        var name = context.getFunctionDefinitionName() == null ? new SchemeSymbol("anonymous_procedure") : context.getFunctionDefinitionName();
        var rootNode = new ProcedureRootNode(name, context.getLanguage(), frameDescriptor, bodyExpressions, argumentsIndex);
        var hasOptionalArgs = params instanceof SchemePair;
        return new LambdaExprNode(rootNode.getCallTarget(), lambdaContext.getNumberOfLambdaParameters(), hasOptionalArgs);
    }

    private static void updateParsingContext(Object params, ParsingContext context) {
        if (params instanceof SchemeCell list) {
            for (Object obj : list) {
                context.addLambdaParameter((SchemeSymbol) obj);
            }
        } else if (params instanceof SchemePair pair) {
            var currentPair = pair;
            while (currentPair.second() instanceof SchemePair) {
                context.addLambdaParameter((SchemeSymbol) currentPair.first());
                currentPair = (SchemePair) pair.second();
            }
            context.addLambdaParameter((SchemeSymbol) currentPair.first());
            context.addLambdaParameter((SchemeSymbol) currentPair.second());
        }

    }

    // (lambda (arg1 ... argN) expr1 ..exprN)
    private static void validate(SchemeCell lambdaList) {
        var params = lambdaList.cdr.car;
        var body = lambdaList.cdr.cdr;

        if (params instanceof SchemeCell list) {
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

        if (body.isEmpty()) {
            throw new SchemeException("lambda: no expression in body", null);
        }
    }
}
