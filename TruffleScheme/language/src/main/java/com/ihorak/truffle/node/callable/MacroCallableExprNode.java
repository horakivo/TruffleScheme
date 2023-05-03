package com.ihorak.truffle.node.callable;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.ihorak.truffle.runtime.SchemeSymbol;
import com.ihorak.truffle.runtime.UserDefinedProcedure;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Executed;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;

import java.util.List;

public abstract class MacroCallableExprNode extends SchemeExpression {

    private final Object[] notEvaluatedArgs;
    private final ParsingContext parsingContext;
    private final SchemeSymbol name;

    @Child
    @Executed
    protected SchemeExpression transformationExpr;

    public MacroCallableExprNode(SchemeExpression transformationExpr, List<Object> notEvaluatedArgs, ParsingContext parsingContext, SchemeSymbol name) {
        this.notEvaluatedArgs = notEvaluatedArgs.toArray();
        this.transformationExpr = transformationExpr;
        this.parsingContext = parsingContext;
        this.name = name;
    }

    @Specialization
    protected Object doMacroExpansion(VirtualFrame frame,
                                      UserDefinedProcedure userDefinedProcedure,
                                      @Cached DispatchUserProcedureNode dispatchUserProcedureNode) {
        CompilerDirectives.transferToInterpreterAndInvalidate();

        if (userDefinedProcedure.expectedNumberOfArgs() != notEvaluatedArgs.length) {
            throw SchemeException.arityException(this, name.value(), userDefinedProcedure.expectedNumberOfArgs(), notEvaluatedArgs.length);
        }

        var args = getArgumentsForMacroExpansion(userDefinedProcedure);
        var macroExpandedIR = dispatchUserProcedureNode.executeDispatch(userDefinedProcedure, args);
        var macroExpandedTruffleAST = InternalRepresentationConverter.convert(macroExpandedIR, parsingContext, false, false, null);
        return replace(macroExpandedTruffleAST).executeGeneric(frame);
    }


    @TruffleBoundary
    @Specialization
    Object fallback(Object object) {
        throw new SchemeException("""
                macro's body has to be evaluated procedure
                expected: procedure?
                given: %s""".formatted(object), this);
    }

    private Object[] getArgumentsForMacroExpansion(UserDefinedProcedure procedure) {
        var arguments = new Object[notEvaluatedArgs.length + 1];
        arguments[0] = procedure.parentFrame();

        int index = 1;
        for (Object argument : notEvaluatedArgs) {
            arguments[index] = argument;
            index++;
        }

        return arguments;
    }
}
