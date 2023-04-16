package com.ihorak.truffle.runtime;

import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;



@ExportLibrary(InteropLibrary.class)
public record PrimitiveProcedure(
        String name,
        NodeFactory<? extends AlwaysInlinableProcedureNode> factory
) implements TruffleObject {


//----------------InteropLibrary messages–----------------------
}
