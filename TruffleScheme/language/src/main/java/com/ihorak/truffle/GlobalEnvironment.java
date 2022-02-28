package com.ihorak.truffle;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.ReadProcedureArgExprNode;
import com.ihorak.truffle.node.exprs.arithmetic.*;
import com.ihorak.truffle.node.exprs.builtin.EvalExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.arithmetic.*;
import com.ihorak.truffle.type.SchemeFunction;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;

import java.util.Map;


public class GlobalEnvironment {

    private VirtualFrame globalVirtualFrame;
    private MaterializedFrame globalMaterializedFrame;

    public GlobalEnvironment() {
        this.globalVirtualFrame = createGlobalVirtualFrame();
        //this.globalMaterializedFrame = globalVirtualFrame.materialize();
    }

    private static void addPrimitiveProcedures(VirtualFrame frame) {
        var builtinFunction = getAllBuiltinFunctions();

        for (SchemeSymbol symbol : builtinFunction.keySet()) {
            var index = frame.getFrameDescriptor().findOrAddAuxiliarySlot(symbol);
            frame.setAuxiliarySlot(index, builtinFunction.get(symbol));
        }
    }


    private VirtualFrame createGlobalVirtualFrame() {
        var globalFrameDescriptor = FrameDescriptor.newBuilder();
        var globalVirtualFrame = Truffle.getRuntime().createVirtualFrame(new Object[]{}, globalFrameDescriptor.build());
        var builtinFunction = getAllBuiltinFunctions();

        for (SchemeSymbol symbol : builtinFunction.keySet()) {
            var index = globalVirtualFrame.getFrameDescriptor().findOrAddAuxiliarySlot(symbol);
            globalVirtualFrame.setAuxiliarySlot(index, builtinFunction.get(symbol));
        }

        return globalVirtualFrame;
    }

    private static Map<SchemeSymbol, SchemeFunction> getAllBuiltinFunctions() {
        var plusExpr = ReducePlusExprRuntimeNodeGen.create(PlusExprNodeGen.create());
        SchemeFunction plusFunction = SchemeFunction.createBuiltinFunction(plusExpr, null);
        var minusExpr = ReduceMinusExprRuntimeNodeGen.create(MinusExprNodeGen.create());
        SchemeFunction minusFunction = SchemeFunction.createBuiltinFunction(minusExpr, null);
        var multiplyExpr = ReduceMultiplyExprRuntimeNodeGen.create(MultiplyExprNodeGen.create());
        SchemeFunction multiplyFunction = SchemeFunction.createBuiltinFunction(multiplyExpr, null);
        var divideExpr = ReduceDivideExprRuntimeNodeGen.create(DivideExprNodeGen.create());
        SchemeFunction divideFunction = SchemeFunction.createBuiltinFunction(divideExpr, null);
        SchemeExpression evalExpr = EvalExprNodeGen.create(new ReadProcedureArgExprNode(0));
        SchemeFunction evalFunction = SchemeFunction.createBuiltinFunction(evalExpr, 1);


        return Map.of(
                new SchemeSymbol("+"), plusFunction,
                new SchemeSymbol("-"), minusFunction,
                new SchemeSymbol("*"), multiplyFunction,
                new SchemeSymbol("/"), divideFunction
        );
    }



    public VirtualFrame getGlobalVirtualFrame() {
        return globalVirtualFrame;
    }

    public MaterializedFrame getGlobalMaterializedFrame() {
        return globalMaterializedFrame;
    }

}
