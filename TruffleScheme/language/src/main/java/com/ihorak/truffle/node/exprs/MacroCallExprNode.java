package com.ihorak.truffle.node.exprs;

import com.ihorak.truffle.context.Context;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.ProcedureDispatchNode;
import com.ihorak.truffle.node.ProcedureDispatchNodeGen;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.parser.ListToExpressionConverter;
import com.ihorak.truffle.type.SchemeFunction;
import com.ihorak.truffle.type.SchemeMacro;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.profiles.BranchProfile;
import com.oracle.truffle.api.profiles.ConditionProfile;

import java.util.List;

public class MacroCallExprNode extends SchemeExpression {

    @Child
    private SchemeExpression macroExpr;
    @Children
    private final SchemeExpression[] arguments;
    @Child
    private ProcedureDispatchNode dispatchNode;
    private final Context context;

    private final BranchProfile noMarcoAsFirstArgumentProfile = BranchProfile.create();
    private final BranchProfile noParentProfile = BranchProfile.create();

    public MacroCallExprNode(SchemeExpression macroExpr, List<SchemeExpression> arguments, Context context) {
        this.macroExpr = macroExpr;
        this.arguments = arguments.toArray(SchemeExpression[]::new);
        this.dispatchNode = ProcedureDispatchNodeGen.create();
        this.context = context;
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        var macro = getMacro(virtualFrame);
        var macroArguments = getMarcoArguments(macro.getTransformationProcedure(), virtualFrame);

        var transformedData = applyTransformationProcedure(macro.getTransformationProcedure(), macroArguments);

        return ListToExpressionConverter.convert(transformedData, context).executeGeneric(virtualFrame);
    }

    private Object applyTransformationProcedure(SchemeFunction transformationProcedure, Object[] arguments) {
         return dispatchNode.executeDispatch(transformationProcedure.getCallTarget(), arguments);
    }

    private SchemeMacro getMacro(VirtualFrame frame) {
        try {
            return macroExpr.executeMacro(frame);
        } catch (UnexpectedResultException e) {
            noMarcoAsFirstArgumentProfile.enter();
            throw new SchemeException("application: not a macro;\nexpected a macro that can be applied to arguments\ngiven: " + e.getResult());
        }
    }

    @ExplodeLoop
    private Object[] getMarcoArguments(SchemeFunction function, VirtualFrame parentFrame) {
        Object[] arguments = new Object[this.arguments.length + 1];
        if (function.getParentFrame() == null) {
            noParentProfile.enter();
            throw new SchemeException("macro: no parent in lambda! Interpreter mistake!");
        }
        arguments[0] = function.getParentFrame();


        int index = 1;
        for (SchemeExpression expression : this.arguments) {
            arguments[index] = expression.executeGeneric(parentFrame);
            index++;
        }

        return arguments;
    }
}
