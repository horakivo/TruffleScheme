// CheckStyle: start generated
package com.ihorak.truffle.node.literals;

import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeCost;

@GeneratedBy(SymbolExprNode.class)
public final class SymbolExprNodeGen extends SymbolExprNode {

    private final SchemeSymbol symbol;

    private SymbolExprNodeGen(SchemeSymbol symbol) {
        this.symbol = symbol;
    }

    @Override
    protected SchemeSymbol getSymbol() {
        return this.symbol;
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        return read(frameValue);
    }

    @Override
    public NodeCost getCost() {
        return NodeCost.MONOMORPHIC;
    }

    public static SymbolExprNode create(SchemeSymbol symbol) {
        return new SymbolExprNodeGen(symbol);
    }

}
