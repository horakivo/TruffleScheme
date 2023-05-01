package com.ihorak.truffle.node.scope;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.runtime.PrimitiveProcedure;
import com.ihorak.truffle.runtime.SchemeSymbol;
import com.ihorak.truffle.runtime.UndefinedValue;
import com.ihorak.truffle.runtime.UserDefinedProcedure;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;

@NodeChild(value = "valueToStore")
public abstract class WriteGlobalVariableExprNode extends SchemeExpression {

    private final SchemeSymbol symbol;

    public WriteGlobalVariableExprNode(SchemeSymbol symbol) {
        this.symbol = symbol;
    }

    @Specialization
    protected Object storeProcedure(UserDefinedProcedure procedure) {
        getContext().addUserDefinedProcedure(symbol, procedure);
        return UndefinedValue.SINGLETON;
    }

    @Specialization
    protected Object storeProcedure(PrimitiveProcedure procedure) {
        getContext().addPrimitiveProcedure(symbol, procedure);
        return UndefinedValue.SINGLETON;
    }

    @Fallback
    protected Object storeObject(Object object) {
        getContext().addVariable(symbol, object);
        return UndefinedValue.SINGLETON;
    }
}
