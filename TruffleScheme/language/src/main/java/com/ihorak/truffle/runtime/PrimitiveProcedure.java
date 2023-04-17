package com.ihorak.truffle.runtime;

import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.ihorak.truffle.node.callable.DispatchNode;
import com.ihorak.truffle.node.callable.DispatchPrimitiveProcedureNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;


@ExportLibrary(InteropLibrary.class)
public record PrimitiveProcedure(
        String name,
        NodeFactory<? extends AlwaysInlinableProcedureNode> factory
) implements TruffleObject {


//----------------InteropLibrary messages–----------------------

    @ExportMessage
    boolean isExecutable() {
        return true;
    }

    @ExportMessage
    Object execute(Object[] arguments,
                   @Cached DispatchPrimitiveProcedureNode dispatchNode) {
        return dispatchNode.execute(this, arguments);
    }
}
