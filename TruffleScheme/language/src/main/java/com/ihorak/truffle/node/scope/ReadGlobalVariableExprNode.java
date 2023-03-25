package com.ihorak.truffle.node.scope;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.Assumption;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.profiles.ConditionProfile;
import com.oracle.truffle.api.utilities.CyclicAssumption;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.VirtualFrame;

public class ReadGlobalVariableExprNode extends SchemeExpression {

    private final SchemeSymbol symbol;
    @CompilationFinal
    private Object cachedValue;
    public static final CyclicAssumption notRedefinedAssumption = new CyclicAssumption("global variable not redefined");
    @CompilationFinal
    private Assumption cachedAssumption;

    private final ConditionProfile validProfile = ConditionProfile.createBinaryProfile();
    private final ConditionProfile cachedProfile = ConditionProfile.createCountingProfile();


    public ReadGlobalVariableExprNode(SchemeSymbol symbol) {
        this.symbol = symbol;
        cachedAssumption = notRedefinedAssumption.getAssumption();
    }

    @Override
    public Object executeGeneric(final VirtualFrame virtualFrame) {
        if (validProfile.profile(cachedAssumption.isValid())) {
            if (cachedProfile.profile(cachedValue != null)) {
                return cachedValue;
            } else {
                return retrieveAndCachedTheValue();
            }
        } else {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            cachedAssumption = notRedefinedAssumption.getAssumption();
            return retrieveAndCachedTheValue();
        }
    }

    private Object retrieveAndCachedTheValue() {
        CompilerDirectives.transferToInterpreterAndInvalidate();
        cachedValue = getContext().getVariable(symbol);
        return cachedValue;
    }
}
