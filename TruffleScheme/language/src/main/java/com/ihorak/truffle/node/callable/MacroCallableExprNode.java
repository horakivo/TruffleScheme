package com.ihorak.truffle.node.callable;

import com.ihorak.truffle.convertor.ListToExpressionConverter;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

import java.util.List;

public class MacroCallableExprNode extends SchemeExpression {

    private final Object[] notEvaluatedArgs;
    private final ParsingContext parsingContext;

    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private SchemeExpression macroExpr;
    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private DispatchNode dispatchNode = DispatchNodeGen.create();
    @CompilationFinal
    private SchemeExpression macroExpandedTree;

    public MacroCallableExprNode(SchemeExpression macroExpr, List<Object> notEvaluatedArgs, ParsingContext parsingContext) {
        this.macroExpr = macroExpr;
        this.notEvaluatedArgs = notEvaluatedArgs.toArray();
        this.parsingContext = parsingContext;
    }

    @Override
    public Object executeGeneric(final VirtualFrame virtualFrame) {
        try {
            var macro = macroExpr.executeMacro(virtualFrame);

            if (macroExpandedTree == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                var transformationCallTarget = macro.transformationProcedure().getCallTarget();
                var notEvalArgs = getNotEvaluatedArguments(virtualFrame);
                var transformedData = dispatchNode.executeDispatch(transformationCallTarget, notEvalArgs);
                macroExpandedTree = ListToExpressionConverter.convert(transformedData, parsingContext);
            }

            return macroExpandedTree.executeGeneric(virtualFrame);

        } catch (UnexpectedResultException e) {
            throw new SchemeException("Fatal error: This should never happen. Problem with converter", this);
        }
    }

    @ExplodeLoop
    private Object[] getNotEvaluatedArguments(VirtualFrame frame) {
        var args = new Object[notEvaluatedArgs.length + 1];
        args[0] = frame.materialize();

        int index = 1;
        for (Object arg : notEvaluatedArgs) {
            args[index] = arg;
            index++;
        }

        return args;
    }
}
