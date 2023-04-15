//package com.ihorak.truffle.type;

//import com.ihorak.truffle.node.callable.FixedNumberOfArgsBuiltinNode;
//import com.oracle.truffle.api.dsl.NodeFactory;
//import com.oracle.truffle.api.interop.InteropLibrary;
//import com.oracle.truffle.api.interop.TruffleObject;
//import com.oracle.truffle.api.library.ExportLibrary;
//
//@ExportLibrary(InteropLibrary.class)
//public record FixedArgsPrimitiveProcedure(
//        String name,
//        NodeFactory<? extends FixedNumberOfArgsBuiltinNode> factory,
//        int expectedNumberOfArgs
//) implements TruffleObject {
//    public FixedArgsPrimitiveProcedure(String name, NodeFactory<? extends FixedNumberOfArgsBuiltinNode> factory) {
//        this(name, factory, factory.getExecutionSignature().size());
//    }


    //----------------InteropLibrary messages–----------------------
//    @ExportMessage
//    boolean hasLanguage() {
//        return true;
//    }

//}
