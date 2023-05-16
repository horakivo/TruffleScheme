package com.ihorak.truffle.runtime;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.ihorak.truffle.node.callable.DispatchPrimitiveProcedureNode;
import com.ihorak.truffle.node.builtin.polyglot.ForeignToSchemeNode;
import com.oracle.truffle.api.TruffleLanguage;
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
    boolean hasLanguage() {
        return true;
    }

    @ExportMessage
    Class<? extends TruffleLanguage<?>> getLanguage() {
        return SchemeTruffleLanguage.class;
    }

    @ExportMessage
    boolean isExecutable() {
        return true;
    }

    @ExportMessage
    Object execute(Object[] arguments,
                   @Cached ForeignToSchemeNode foreignToSchemeNode,
                   @Cached DispatchPrimitiveProcedureNode dispatchNode) {
        var args = convertToSchemeValues(arguments, foreignToSchemeNode);
        return dispatchNode.execute(this, args);
    }

    @ExportMessage
    Object toDisplayString(@SuppressWarnings("unused") boolean allowSideEffects) {
        return name;
    }

    private Object[] convertToSchemeValues(Object[] argumentsToConvert, ForeignToSchemeNode foreignToSchemeNode) {
        Object[] result = new Object[argumentsToConvert.length];
        for (int i = 0; i < argumentsToConvert.length; i++) {
            result[i] = foreignToSchemeNode.executeConvert(argumentsToConvert[i]);
        }

        return result;
    }
}
