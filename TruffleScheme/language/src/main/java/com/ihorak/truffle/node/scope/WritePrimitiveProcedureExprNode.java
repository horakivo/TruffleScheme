package com.ihorak.truffle.node.scope;

import com.ihorak.truffle.SchemeLanguageContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.PrimitiveProcedure;
import com.ihorak.truffle.type.SchemeSymbol;
import com.ihorak.truffle.type.UndefinedValue;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class WritePrimitiveProcedureExprNode extends SchemeExpression {

    private final PrimitiveProcedure procedure;
    private final SchemeSymbol symbol;

    public WritePrimitiveProcedureExprNode(PrimitiveProcedure procedure, SchemeSymbol symbol) {
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
