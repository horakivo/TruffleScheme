package com.ihorak.truffle.node.scope;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.type.SchemeSymbol;
import com.ihorak.truffle.type.UndefinedValue;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;


public class WriteLocalVariableExprNode extends SchemeExpression {

    @Child private WriteFrameSlotNode writeFrameSlotNode;
    @Child protected SchemeExpression valueNode;
    public final int frameIndex;

    public WriteLocalVariableExprNode(int frameIndex, SchemeExpression valueToStore) {
        this.frameIndex = frameIndex;
        this.writeFrameSlotNode = WriteFrameSlotNodeGen.create(frameIndex);
        this.valueNode = valueToStore;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        final Object value = valueNode.executeGeneric(frame);
        writeFrameSlotNode.executeWrite(frame, value);
        return UndefinedValue.SINGLETON;
    }
}
