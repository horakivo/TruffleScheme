package com.ihorak.truffle.node.special_form.lambda;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeFunction;
import com.ihorak.truffle.type.SchemeSymbol;
import com.ihorak.truffle.type.UndefinedValue;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;

public abstract class WriteBuiltinProcedureExprNode extends SchemeExpression {

    private final SchemeFunction procedure;
    private final SchemeSymbol symbol;

    public WriteBuiltinProcedureExprNode(SchemeFunction procedure, SchemeSymbol symbol) {
        this.procedure = procedure;
        this.symbol = symbol;
    }


    @Specialization
    protected Object writeFunction() {
        var context = SchemeLanguageContext.get(this);
        context.getGlobalState().addVariable(symbol, procedure);

        return UndefinedValue.SINGLETON;
    }
}
