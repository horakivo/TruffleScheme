package com.ihorak.truffle.node.scope;

import com.ihorak.truffle.SchemeLanguageContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.runtime.SchemeSymbol;
import com.oracle.truffle.api.Assumption;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.profiles.ConditionProfile;
import com.oracle.truffle.api.utilities.CyclicAssumption;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.VirtualFrame;

public abstract class ReadGlobalVariableExprNode extends SchemeExpression {

    protected final SchemeSymbol symbol;

    public ReadGlobalVariableExprNode(SchemeSymbol symbol) {
        this.symbol = symbol;
    }

    @Specialization(assumptions = "notRedefinedAssumption")
    protected Object read(VirtualFrame frame,
                          @Cached("getNotRedefinedAssumption()") Assumption notRedefinedAssumption,
                          @Cached("getContext().getVariable(symbol)") Object cachedValue) {
        return cachedValue;
    }

    protected Assumption getNotRedefinedAssumption() {
        return SchemeLanguageContext.notRedefinedAssumption.getAssumption();
    }
}
