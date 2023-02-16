package com.ihorak.truffle.convertor;

import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.util.BuiltinUtils;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.CallableExprNode;
import com.ihorak.truffle.node.callable.MacroCallableExprNode;
import com.ihorak.truffle.node.callable.TCO.SelfRecursiveTailCallThrowerNode;
import com.ihorak.truffle.node.callable.TCO.SelfRecursiveTailCallThrowerNodeGen;
import com.ihorak.truffle.node.callable.TCO.TailCallCatcherNode;
import com.ihorak.truffle.node.callable.TCO.TailCallThrowerNodeGen;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.frame.FrameSlotKind;

import java.util.ArrayList;
import java.util.List;

public class ProcedureCallConverter {

    private ProcedureCallConverter() {
    }

    /*
     *  --> (operand argExpr1 ... argExprN)
     * */
    //here it can be either procedure or builtin
    public static SchemeExpression convertListToProcedureCall(SchemeList procedureList, ParsingContext context, boolean isTailCall) {
        var operand = procedureList.car();
        List<SchemeExpression> arguments = getProcedureArguments(procedureList.cdr(), context);


        if (operand instanceof SchemeSymbol schemeSymbol) {
            if (isUnquote(schemeSymbol))
                throw new SchemeException("unquote: expression not valid outside of quasiquote in form " + procedureList, null);
            if (isUnquoteSplicing(schemeSymbol))
                throw new SchemeException("unquote-splicing: expression not valid outside of quasiquote in form " + procedureList, null);


            if (BuiltinUtils.isBuiltinProcedure(schemeSymbol)) {
                return BuiltinConverter.createBuiltin(schemeSymbol, arguments, context);
            } else if (context.isMacro(schemeSymbol)) {
                List<Object> notEvaluatedArgs = new ArrayList<>();
                procedureList.cdr().forEach(notEvaluatedArgs::add);
                var macroExpr = InternalRepresentationConverter.convert(schemeSymbol, context, false, false);
                return new MacroCallableExprNode(macroExpr, notEvaluatedArgs, context);
            }
        }


        var callable = InternalRepresentationConverter.convert(operand, context, false, false);
        //var callNode = new CallableExprNode(arguments, callable);
//

        if (isTailCall) {
            if (isSelfTailRecursive(operand, context)) {
                return SelfRecursiveTailCallThrowerNodeGen.create(arguments);
            }
            return TailCallThrowerNodeGen.create(arguments, callable);
        } else {
            int tailCallArgumentsSlot = context.getFrameDescriptorBuilder().addSlot(FrameSlotKind.Object, null, null);
            int tailCallTargetSlot = context.getFrameDescriptorBuilder().addSlot(FrameSlotKind.Object, null, null);
            return new TailCallCatcherNode(arguments, callable, tailCallArgumentsSlot, tailCallTargetSlot);
            //return  new CallableExprNode(arguments, callable);
        }

        // return callNode;

    }

    private static boolean isSelfTailRecursive(Object operand, ParsingContext context) {
        var currentlyDefiningMethod = context.getFunctionDefinitionName();
        if (currentlyDefiningMethod == null) return false;

        return operand instanceof SchemeSymbol symbol && symbol.equals(currentlyDefiningMethod);
    }

    private static List<SchemeExpression> getProcedureArguments(SchemeList argumentList, ParsingContext context) {
        List<SchemeExpression> result = new ArrayList<>();

        for (Object obj : argumentList) {
            result.add(InternalRepresentationConverter.convert(obj, context, false, false));
        }
        return result;
    }

    private static boolean isUnquote(SchemeSymbol schemeSymbol) {
        return schemeSymbol.getValue().equals("unquote");
    }

    private static boolean isUnquoteSplicing(SchemeSymbol schemeSymbol) {
        return schemeSymbol.getValue().equals("unquote-splicing");
    }
}
