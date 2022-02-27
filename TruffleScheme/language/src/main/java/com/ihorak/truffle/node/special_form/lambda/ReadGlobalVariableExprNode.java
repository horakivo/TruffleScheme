package com.ihorak.truffle.node.special_form.lambda;

import com.ihorak.truffle.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.literals.SymbolExprNodeGen;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;

import static com.ihorak.truffle.node.special_form.lambda.FrameUtil.findAuxiliaryValue;
import static com.ihorak.truffle.node.special_form.lambda.FrameUtil.findGlobalEnv;


@NodeField(name = "symbol", type = SchemeSymbol.class)
@NodeField(name = "frameSlotIndex", type = int.class)
public abstract class ReadGlobalVariableExprNode extends SchemeExpression {

    protected abstract int getFrameSlotIndex();

    protected abstract SchemeSymbol getSymbol();

    @Specialization(guards = "isLong(frame)")
    protected long readLong(VirtualFrame frame) {
        return findGlobalEnv(frame).getLong(getFrameSlotIndex());
    }

    @Specialization(guards = "isBoolean(frame)")
    protected boolean readBoolean(VirtualFrame frame) {
        return findGlobalEnv(frame).getBoolean(getFrameSlotIndex());
    }

    @Specialization(guards = "isDouble(frame)")
    protected double readDouble(VirtualFrame frame) {
        return findGlobalEnv(frame).getDouble(getFrameSlotIndex());
    }

    @Specialization(guards = "isObject(frame)", replaces = {"readLong", "readBoolean", "readDouble"})
    protected Object read(VirtualFrame frame) {
        return findGlobalEnv(frame).getObject(getFrameSlotIndex());
    }

    @Specialization(replaces = "read")
    protected Object readRuntimeVariable(VirtualFrame frame) {
        return findAuxiliaryValue(frame, getSymbol());
    }

//    @Fallback
//    protected void fallback(VirtualFrame frame) {
//        throw new SchemeException(getSymbol() + ": undefined\ncannot reference an identifier before its definition. FrameSlotKind: " + findGlobalEnv(frame).getFrameDescriptor().getSlotKind(getFrameSlotIndex()));
//    }


    public boolean isLong(VirtualFrame frame) {
        return findGlobalEnv(frame).getFrameDescriptor().getSlotKind(getFrameSlotIndex()) == FrameSlotKind.Long;
    }

    public boolean isBoolean(VirtualFrame frame) {
        return findGlobalEnv(frame).getFrameDescriptor().getSlotKind(getFrameSlotIndex()) == FrameSlotKind.Boolean;
    }

    public boolean isDouble(VirtualFrame frame) {
        return findGlobalEnv(frame).getFrameDescriptor().getSlotKind(getFrameSlotIndex()) == FrameSlotKind.Double;
    }

    public boolean isObject(VirtualFrame frame) {
        return findGlobalEnv(frame).getFrameDescriptor().getSlotKind(getFrameSlotIndex()) == FrameSlotKind.Object;
    }

}
