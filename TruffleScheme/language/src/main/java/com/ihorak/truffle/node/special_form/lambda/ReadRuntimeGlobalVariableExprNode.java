package com.ihorak.truffle.node.special_form.lambda;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.Assumption;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;

public abstract class ReadRuntimeGlobalVariableExprNode extends SchemeExpression {

    private final SchemeSymbol symbol;
    @CompilationFinal
    private Object cache;
    public static final Assumption notRedefinedAssumption = Truffle.getRuntime().createAssumption();

    public ReadRuntimeGlobalVariableExprNode(SchemeSymbol symbol) {
        this.symbol = symbol;
    }

    @Specialization
    protected Object readGlobalVariable() {
        if (notRedefinedAssumption.isValid()) {
            if (cache == null) {
                cache = retrieveValueFromContext();
            }
        } else {
            cache = retrieveValueFromContext();
        }
        return cache;
    }

    private Object retrieveValueFromContext() {
        CompilerDirectives.transferToInterpreterAndInvalidate();
        var context = SchemeLanguageContext.get(this);
        return context.getGlobalState().getVariable(symbol);
    }
}
