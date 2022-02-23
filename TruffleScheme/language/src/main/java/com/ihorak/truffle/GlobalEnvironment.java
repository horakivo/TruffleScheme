package com.ihorak.truffle;

import com.ihorak.truffle.node.exprs.arithmetic.*;
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
        this.globalMaterializedFrame = globalVirtualFrame.materialize();
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


//        for (int i = 0; i < frameSlotIndexes.size(); i++) {
//            globalVirtualFrame.setObject(frameSlotIndexes.get(i), builtinFunction.get(i));
//        }

        return globalVirtualFrame;


//        var plusFrameSlot = globalFrameDescriptor.addFrameSlot(new SchemeSymbol("+"), FrameSlotKind.Object);
//        var minusFrameSlot = globalFrameDescriptor.addFrameSlot(new SchemeSymbol("-"), FrameSlotKind.Object);
//        var multiplyFrameSlot = globalFrameDescriptor.addFrameSlot(new SchemeSymbol("*"), FrameSlotKind.Object);
//        var divideFrameSlot = globalFrameDescriptor.addFrameSlot(new SchemeSymbol("/"), FrameSlotKind.Object);
//        var evalFrameSlot = globalFrameDescriptor.addFrameSlot(new SchemeSymbol("eval"), FrameSlotKind.Object);
//        var test = globalFrameDescriptor.addFrameSlot(new SchemeSymbol("test"), FrameSlotKind.Boolean);
//
//
//        //globalVirtualFrame.setObject(plusFrameSlot, plusFunction);
//        globalVirtualFrame.setBo
//        globalVirtualFrame.setObject(minusFrameSlot, minusFunction);
//        globalVirtualFrame.setObject(multiplyFrameSlot, multiplyFunction);
//        globalVirtualFrame.setObject(divideFrameSlot, divideFunction);
//        globalVirtualFrame.setObject(evalFrameSlot, evalFunction);
//        globalVirtualFrame.setBoolean(test, true);

    }

    private static Map<SchemeSymbol, SchemeFunction> getAllBuiltinFunctions() {
        var plusExpr = ReducePlusExprNodeGen.create(PlusExprRuntimeNodeGen.create());
        SchemeFunction plusFunction = SchemeFunction.createBuiltinFunction(plusExpr, null);
        var minusExpr = ReduceMinusExprNodeGen.create(MinusExprRuntimeNodeGen.create());
        SchemeFunction minusFunction = SchemeFunction.createBuiltinFunction(minusExpr, null);
        var multiplyExpr = ReduceMultiplyExprNodeGen.create(MultiplicationExprRuntimeNodeGen.create());
        SchemeFunction multiplyFunction = SchemeFunction.createBuiltinFunction(multiplyExpr, null);
        var divideExpr = ReduceDivideExprNodeGen.create(DivideExprRuntimeNodeGen.create());
        SchemeFunction divideFunction = SchemeFunction.createBuiltinFunction(divideExpr, null);
        //eval
//        SchemeExpression evalExpr = EvalExprNodeGen.create(new ReadProcedureArgExprNode(0));
//        SchemeFunction evalFunction = SchemeFunction.createBuiltinFunction(evalExpr, 1);


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
