package com.ihorak.truffle.type;

import com.ihorak.truffle.node.callable.AlwaysInlinedMethodNode;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@ExportLibrary(InteropLibrary.class)
public record FixedArgsPrimitiveProcedure(
        String name,
        NodeFactory<? extends AlwaysInlinedMethodNode> factory,
        int expectedNumberOfArgs
) implements TruffleObject {
    public FixedArgsPrimitiveProcedure(String name, NodeFactory<? extends AlwaysInlinedMethodNode> factory) {
        this(name, factory, factory.getExecutionSignature().size());
    }


    //----------------InteropLibrary messages–----------------------
//    @ExportMessage
//    boolean hasLanguage() {
//        return true;
//    }

}
