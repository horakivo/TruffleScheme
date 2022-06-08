package com.ihorak.truffle.node.scope;

import com.ihorak.truffle.SchemeLanguageContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;
import com.ihorak.truffle.type.UndefinedValue;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;

@NodeChild(value = "valueToStore")
public abstract class WriteGlobalVariableExprNode extends SchemeExpression {

    private final SchemeSymbol symbol;

    public WriteGlobalVariableExprNode(SchemeSymbol symbol) {
        this.symbol = symbol;
    }

    //TODO Truffle boundary here?
    @Specialization
    protected UndefinedValue assignVariable(Object valueToStore) {
        getCurrentLanguageContext().getGlobalState().addVariable(symbol, valueToStore);
        return UndefinedValue.SINGLETON;
    }

}
