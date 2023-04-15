package com.ihorak.truffle.type;

import com.ihorak.truffle.node.callable.AlwaysInlinedMethodNode;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;


/**
 * This Scheme type exist because I wanted to have seperated user define procedures from primitive ones
 * The reason behind it, is that user defined procedures will always have `numberOfArgs` specified (therefore it can be int instead of Integer)
 * Here it has to be Integer because there are primitive types (+, - ...) which take arbitrary number of args.
 * <p>
 * Another reason is that this type doesn't have any VirtualFrame parent since this procedures do NOT create a VirtualFrame (in this implementation they do unfortunately, because I can't come up with better solution right now)
 */
@ExportLibrary(InteropLibrary.class)
public record ArbitraryArgsPrimitiveProcedure(
        String name,
        NodeFactory<? extends AlwaysInlinedMethodNode> alwaysInlinableFactoryNode
) implements TruffleObject {


//----------------InteropLibrary messages–----------------------
}
