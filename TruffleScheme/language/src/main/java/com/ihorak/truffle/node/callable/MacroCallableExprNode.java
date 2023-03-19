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

    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private DirectCallNode directDispatchNode;
    @CompilationFinal
    private SchemeExpression macroExpandedTree;

    public MacroCallableExprNode(CallTarget transformationProcedure, List<Object> notEvaluatedArgs, ParsingContext parsingContext) {
        this.notEvaluatedArgs = new Object[notEvaluatedArgs.size() + 1];
        int index = 1;
        for (Object obj : notEvaluatedArgs) {
            this.notEvaluatedArgs[index] = obj;
            index++;
        }

        this.parsingContext = parsingContext;
        this.directDispatchNode = DirectCallNode.create(transformationProcedure);
    }

    //TODO zde mozna udelat insert (nebo neco jako replace, kdy expandovane makro nahradim proste)
    @Override
    public Object executeGeneric(final VirtualFrame frame) {

        if (macroExpandedTree == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            notEvaluatedArgs[0] = frame.materialize();
            var transformedData = directDispatchNode.call(notEvaluatedArgs);
            //TODO try replace
            macroExpandedTree = InternalRepresentationConverter.convert(transformedData, parsingContext, false, false, null);
        }

        return macroExpandedTree.executeGeneric(frame);
    }
}
