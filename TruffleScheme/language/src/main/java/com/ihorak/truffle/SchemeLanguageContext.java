package com.ihorak.truffle;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.scope.ReadGlobalVariableExprNode;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.nodes.Node;

import java.util.Map;

public class SchemeLanguageContext {

    private final Map<SchemeSymbol, Object> globalVariableStorage;

    public SchemeLanguageContext(SchemeTruffleLanguage language) {
        this.globalVariableStorage = PrimitiveProcedureGenerator.generate(language);
    }

    private static final TruffleLanguage.ContextReference<SchemeLanguageContext> REFERENCE =
            TruffleLanguage.ContextReference.create(SchemeTruffleLanguage.class);

    public static SchemeLanguageContext get(Node node) {
        return REFERENCE.get(node);
    }

    public Object getVariable(SchemeSymbol symbol) {
        var value = globalVariableStorage.get(symbol);
        if (value == null) {
            throw new SchemeException(symbol + ": undefined\ncannot reference an identifier before its definition", null);
        }
        return value;
    }

    public void addVariable(SchemeSymbol symbol, Object valueToStore) {
        var shouldInvalidate = globalVariableStorage.containsKey(symbol);
        globalVariableStorage.put(symbol, valueToStore);
        if (shouldInvalidate) ReadGlobalVariableExprNode.notRedefinedAssumption.invalidate();

    }
}
