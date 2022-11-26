package com.ihorak.truffle.convertor.util;

import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.scope.WriteGlobalVariableExprNode;
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNode;
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNodeGen;
import com.ihorak.truffle.type.SchemeSymbol;

public class CreateWriteExprNode {

    private CreateWriteExprNode() {}

    public static WriteLocalVariableExprNode createWriteLocalVariableExprNode(SchemeSymbol name, SchemeExpression valueToWrite, ParsingContext context) {
        var index = context.findOrAddLocalSymbol(name);
        return WriteLocalVariableExprNodeGen.create(index, name, valueToWrite);
    }

    public static SchemeExpression createWriteGlobalVariableExprNode(SchemeSymbol name, SchemeExpression valueToStore) {
        return new WriteGlobalVariableExprNode(valueToStore, name);
    }

}
