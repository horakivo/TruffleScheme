package com.ihorak.truffle.node.polyglot.TCO;

import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.callable.TCO.exceptions.PolyglotTailCallException;
import com.ihorak.truffle.node.polyglot.PolyglotException;
import com.ihorak.truffle.node.scope.WriteFrameSlotNode;
import com.ihorak.truffle.node.scope.WriteFrameSlotNodeGen;
import com.ihorak.truffle.type.UserDefinedProcedure;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.InteropException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.LibraryFactory;
import com.oracle.truffle.api.nodes.RepeatingNode;

public class PolyglotTailCallLoopNode extends SchemeNode implements RepeatingNode {

    @Child private InteropLibrary interopLibrary;
    @Child private WriteFrameSlotNode writeFrameSlotNode;

    private final int tailCallArgumentsSlot;
    private final int tailCallTargetSlot;

    public PolyglotTailCallLoopNode(int tailCallArgumentsSlot, int tailCallTargetSlot, int tailCallResultSlot) {
        this.interopLibrary = LibraryFactory.resolve(InteropLibrary.class).createDispatched(3);
        this.tailCallArgumentsSlot = tailCallArgumentsSlot;
        this.tailCallTargetSlot = tailCallTargetSlot;
        this.writeFrameSlotNode = WriteFrameSlotNodeGen.create(tailCallResultSlot);
    }

    @Override
    public boolean executeRepeating(VirtualFrame frame) {
        Object[] arguments = (Object[]) frame.getObject(tailCallArgumentsSlot);
        Object foreignProcedure = frame.getObject(tailCallTargetSlot);
        try {
            var result = interopLibrary.execute(foreignProcedure, arguments);
            writeFrameSlotNode.executeWrite(frame, result);
            return false;
        } catch (PolyglotTailCallException e) {
            frame.setObject(tailCallArgumentsSlot, e.getArguments());
            frame.setObject(tailCallTargetSlot, e.getPolyglotProcedure());
            return true;
        } catch (InteropException e) {
            throw PolyglotException.executeException(e, foreignProcedure, arguments.length, this);
        }
    }
}
