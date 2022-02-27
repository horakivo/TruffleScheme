package com.ihorak.truffle.node.literals;

import com.ihorak.truffle.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import static com.ihorak.truffle.node.special_form.lambda.FrameUtil.findAuxiliaryValue;

@NodeField(name = "symbol", type = SchemeSymbol.class)
public abstract class SymbolExprNode extends SchemeExpression {

    protected abstract SchemeSymbol getSymbol();

    @Specialization
    protected Object read(VirtualFrame frame) {
        return findAuxiliaryValue(frame, getSymbol());
    }

}
