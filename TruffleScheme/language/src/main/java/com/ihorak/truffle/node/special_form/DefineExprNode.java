package com.ihorak.truffle.node.special_form;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeChild(value = "valueToStore")
@NodeField(name = "identifier", type = SchemeSymbol.class)
public abstract class DefineExprNode extends SchemeExpression {

    protected abstract SchemeSymbol getIdentifier();

    @Specialization
    protected Object write(VirtualFrame frame, Object value) {
        var index = frame.getFrameDescriptor().findOrAddAuxiliarySlot(getIdentifier());
        frame.setAuxiliarySlot(index, value);
        return null;
    }
}
