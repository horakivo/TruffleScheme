package com.ihorak.truffle;

import com.ihorak.truffle.node.exprs.builtin.ReduceRuntimeExprNode;
import com.ihorak.truffle.node.exprs.builtin.arithmetic.DivideExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.arithmetic.MultiplyExprNodeGen;
import com.ihorak.truffle.type.SchemeFunction;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;

import java.util.ArrayList;
import java.util.List;


public class GlobalEnvironment {

    private VirtualFrame globalVirtualFrame;
    private MaterializedFrame globalMaterializedFrame;

    public GlobalEnvironment() {
        this.globalVirtualFrame = createGlobalVirtualFrame();
        this.globalMaterializedFrame = globalVirtualFrame.materialize();
    }


    private VirtualFrame createGlobalVirtualFrame() {
        var globalFrameDescriptor = FrameDescriptor.newBuilder();
        var builtinFunction = getAllBuiltinFunctions();
        List<Integer> frameSlotIndexes = new ArrayList<>();
//        for (SchemeFunction function : builtinFunction) {
//            frameSlotIndexes.add(globalFrameDescriptor.addSlot(FrameSlotKind.Object, null, null));
//        }

        var globalVirtualFrame = Truffle.getRuntime().createVirtualFrame(new Object[]{}, globalFrameDescriptor.build());

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

    private List<SchemeFunction> getAllBuiltinFunctions() {
        //        var plusExpr = new ReduceRuntimeExprNode(PlusExprNodeGen.create(), 0L, true);
//        SchemeFunction plusFunction = SchemeFunction.createBuiltinFunction(plusExpr, null);
//        var minusExpr = new ReduceRuntimeExprNode(MinusExprNodeGen.create(), 0L, false);
//        SchemeFunction minusFunction = SchemeFunction.createBuiltinFunction(minusExpr, null);
        var multiplyExpr = new ReduceRuntimeExprNode(MultiplyExprNodeGen.create(), 1L, true);
        SchemeFunction multiplyFunction = SchemeFunction.createBuiltinFunction(multiplyExpr, null);
        var divideExpr = new ReduceRuntimeExprNode(DivideExprNodeGen.create(), 1L, false);
        SchemeFunction divideFunction = SchemeFunction.createBuiltinFunction(divideExpr, null);
        //eval
//        SchemeExpression evalExpr = EvalExprNodeGen.create(new ReadProcedureArgExprNode(0));
//        SchemeFunction evalFunction = SchemeFunction.createBuiltinFunction(evalExpr, 1);


        return List.of(multiplyFunction, divideFunction);
    }

    public VirtualFrame getGlobalVirtualFrame() {
        return globalVirtualFrame;
    }

    public MaterializedFrame getGlobalMaterializedFrame() {
        return globalMaterializedFrame;
    }

}
