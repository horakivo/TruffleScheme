package com.ihorak.truffle;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.ReadProcedureArgExprNode;
import com.ihorak.truffle.node.exprs.arithmetic.ReduceDivideExprRuntimeNodeGen;
import com.ihorak.truffle.node.exprs.arithmetic.ReduceMinusExprRuntimeNodeGen;
import com.ihorak.truffle.node.exprs.arithmetic.ReduceMultiplyExprRuntimeNodeGen;
import com.ihorak.truffle.node.exprs.arithmetic.ReducePlusExprRuntimeNodeGen;
import com.ihorak.truffle.node.exprs.builtin.EvalExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.arithmetic.DivideExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.arithmetic.MinusExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.arithmetic.MultiplyExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.arithmetic.PlusExprNodeGen;
import com.ihorak.truffle.node.special_form.lambda.WriteBuiltinProcedureExprNode;
import com.ihorak.truffle.node.special_form.lambda.WriteBuiltinProcedureExprNodeGen;
import com.ihorak.truffle.type.SchemeFunction;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlotKind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultPrimitiveProcedures {


    public static DefaultPrimitiveProceduresContainer generate() {
        Map<SchemeSymbol, Integer> map = new HashMap<>();
        var frameDescriptorBuilder = FrameDescriptor.newBuilder();
        List<WriteBuiltinProcedureExprNode> writeBuiltinProcedureExprNodes = new ArrayList<>();

        var builtinFunctionsMap = getAllPrimitiveProcedures();

        for (SchemeSymbol symbol : builtinFunctionsMap.keySet()) {
            int frameIndex = frameDescriptorBuilder.addSlot(FrameSlotKind.Object, symbol, null);
            map.put(symbol, frameIndex);
            writeBuiltinProcedureExprNodes.add(WriteBuiltinProcedureExprNodeGen.create(builtinFunctionsMap.get(symbol), frameIndex, symbol));
        }

        return new DefaultPrimitiveProceduresContainer(map, frameDescriptorBuilder, writeBuiltinProcedureExprNodes);
    }


    private static Map<SchemeSymbol, SchemeFunction> getAllPrimitiveProcedures() {
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

    public static final class DefaultPrimitiveProceduresContainer {
        private final Map<SchemeSymbol, Integer> map;
        private final FrameDescriptor.Builder frameDescriptorBuilder;
        private final List<WriteBuiltinProcedureExprNode> writeExpressions;

        public DefaultPrimitiveProceduresContainer(Map<SchemeSymbol, Integer> map, FrameDescriptor.Builder frameDescriptorBuilder, List<WriteBuiltinProcedureExprNode> writeExpressions) {
            this.map = map;
            this.frameDescriptorBuilder = frameDescriptorBuilder;
            this.writeExpressions = writeExpressions;
        }

        public Map<SchemeSymbol, Integer> getMap() {
            return map;
        }

        public FrameDescriptor.Builder getFrameDescriptorBuilder() {
            return frameDescriptorBuilder;
        }

        public List<WriteBuiltinProcedureExprNode> getWriteExpressions() {
            return writeExpressions;
        }
    }
}
