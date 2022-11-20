package com.ihorak.truffle.node.scope;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;
import com.ihorak.truffle.type.UndefinedValue;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.frame.VirtualFrame;

public class WriteGlobalVariableExprNode extends SchemeExpression {

    private final SchemeSymbol symbol;
    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private SchemeExpression valueToStore;

    public WriteGlobalVariableExprNode(SchemeExpression valueToStore, SchemeSymbol symbol) {
        this.symbol = symbol;
        this.valueToStore = valueToStore;
    }

   // @CompilerDirectives.TruffleBoundary
    //TODO Truffle boundary can't be here but transfer to interprate and invalidate can be. Is this correct?
    @Override
    public Object executeGeneric(final VirtualFrame virtualFrame) {
        CompilerDirectives.transferToInterpreterAndInvalidate();
        getCurrentLanguageContext().getGlobalState().addVariable(symbol, valueToStore.executeGeneric(virtualFrame));
        return UndefinedValue.SINGLETON;
    }

}
