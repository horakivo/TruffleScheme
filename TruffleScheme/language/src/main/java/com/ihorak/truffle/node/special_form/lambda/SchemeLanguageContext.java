package com.ihorak.truffle.node.special_form.lambda;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.CompilerDirectives;

import java.util.HashMap;
import java.util.Map;

public class SchemeLanguageContext {

    private final Map<SchemeSymbol, Object> globalVariableStorage = new HashMap<>();


    public Object getVariable(SchemeSymbol symbol) {
        var value = globalVariableStorage.get(symbol);
        if (value == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw new SchemeException(symbol + ": undefined\ncannot reference an identifier before its definition");
        }
        return value;
    }

    public void storeVariable(SchemeSymbol symbol, Object valueToStore) {
        globalVariableStorage.put(symbol, valueToStore);
    }
}
