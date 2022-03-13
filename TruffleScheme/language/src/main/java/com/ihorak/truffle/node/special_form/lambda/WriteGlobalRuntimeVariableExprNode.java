package com.ihorak.truffle.node.special_form.lambda;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;
import com.ihorak.truffle.type.UndefinedValue;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;

@NodeChild(value = "valueToStore")
public abstract class WriteGlobalRuntimeVariableExprNode extends SchemeExpression {

    private final SchemeSymbol symbol;

    public WriteGlobalRuntimeVariableExprNode(SchemeSymbol symbol) {
        this.symbol = symbol;
    }

    @Specialization
    protected UndefinedValue assignVariable(Object valueToStore) {
        var context = SchemeLanguageContext.get(this);
        context.getGlobalState().addVariable(symbol, valueToStore);

        return UndefinedValue.SINGLETON ;
    }

}
