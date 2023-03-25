package com.ihorak.truffle.node.scope;

import com.ihorak.truffle.SchemeLanguageContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;
import com.ihorak.truffle.type.UndefinedValue;
import com.ihorak.truffle.type.UserDefinedProcedure;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;

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

    @Specialization(guards = "!isUserDefinedProcedure(object)")
    protected Object storeObject(Object object) {
        getContext().addVariable(symbol, object);
        return UndefinedValue.SINGLETON;
    }

    protected boolean isUserDefinedProcedure(Object obj) {
        return obj instanceof UserDefinedProcedure;
    }
}
