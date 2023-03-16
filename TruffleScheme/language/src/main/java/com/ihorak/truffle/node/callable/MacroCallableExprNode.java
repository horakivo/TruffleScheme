package com.ihorak.truffle.node.callable;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

public class MacroCallableExprNode extends SchemeExpression {

    private final Object[] notEvaluatedArgs;
    private final ParsingContext parsingContext;

    private final ParserRuleContext macroCtx;


    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private DirectCallNode directDispatchNode;
    @CompilationFinal
    private SchemeExpression macroExpandedTree;

    public MacroCallableExprNode(CallTarget transformationProcedure, List<Object> notEvaluatedArgs, ParsingContext parsingContext, ParserRuleContext macroCtx) {
        this.notEvaluatedArgs = notEvaluatedArgs.toArray();
        this.parsingContext = parsingContext;
        this.directDispatchNode = DirectCallNode.create(transformationProcedure);
        this.macroCtx = macroCtx;
    }

    //TODO zde mozna udelat insert (nebo neco jako replace, kdy expandovane makro nahradim proste)
    @Override
    public Object executeGeneric(final VirtualFrame virtualFrame) {

        if (macroExpandedTree == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            var notEvalArgs = getNotEvaluatedArguments(virtualFrame);
            var transformedData = directDispatchNode.call(notEvalArgs);
            //TODO try replace
            macroExpandedTree = InternalRepresentationConverter.convert(transformedData, parsingContext, false, false);
        }

        return macroExpandedTree.executeGeneric(virtualFrame);
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
