package com.ihorak.truffle.node.literals;

import com.ihorak.truffle.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@NodeField(name = "symbol", type = SchemeSymbol.class)
public abstract class SymbolExprNode extends SchemeExpression {

    protected abstract SchemeSymbol getSymbol();

    @Specialization
    protected Object read(VirtualFrame frame) {
        return findValue(frame);
    }

    @Nullable
    private Object getParentEnvironment(VirtualFrame virtualFrame) {
        if (virtualFrame.getArguments().length > 0) {
            return virtualFrame.getArguments()[0];
        }
        return null;
    }

    @NotNull
    private Object findValue(VirtualFrame frame) {
        var index = frame.getFrameDescriptor().getAuxiliarySlots().get(getSymbol());

        if (index != null) {
            return frame.getAuxiliarySlot(index);
        }

        var parentFrame = getParentEnvironment(frame);
        if (parentFrame == null) {
            throw new SchemeException(getSymbol() + ": undefined\ncannot reference an identifier before its definition");
        }
        return findValue((VirtualFrame) parentFrame);
    }
}
