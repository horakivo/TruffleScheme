package com.ihorak.truffle.node.scope;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.Assumption;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.utilities.CyclicAssumption;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.VirtualFrame;

public class ReadGlobalVariableExprNode extends SchemeExpression {

    private final SchemeSymbol symbol;
    @CompilationFinal
    private Object cachedValue;
    public static final CyclicAssumption notRedefinedAssumption = new CyclicAssumption("global variable not redefined");
    //TODO do I need this?
    @CompilationFinal
    private Assumption cachedAssumption;


    public ReadGlobalVariableExprNode(SchemeSymbol symbol) {
        this.symbol = symbol;
        cachedAssumption = notRedefinedAssumption.getAssumption();
    }

    @Override
    public Object executeGeneric(final VirtualFrame virtualFrame) {
        if (cachedAssumption.isValid()) {
            if (cachedValue != null) {
                return cachedValue;
            } else {
                return retrieveAndUpdateCachedValueFromLanguageContext();
            }
        } else {
            //TODO Do I need here CompilerDirectives.transferToInterpreterAndInvalidate()?
            cachedAssumption = notRedefinedAssumption.getAssumption();
            return retrieveAndUpdateCachedValueFromLanguageContext();
        }
    }

    private Object retrieveAndUpdateCachedValueFromLanguageContext() {
        CompilerDirectives.transferToInterpreterAndInvalidate();
        cachedValue = getContext().getVariable(symbol);
        return cachedValue;
    }
}
