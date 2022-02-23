// CheckStyle: start generated
package com.ihorak.truffle.node.special_form;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeCost;

@GeneratedBy(DefineExprNode.class)
public final class DefineExprNodeGen extends DefineExprNode {

    private final SchemeSymbol identifier;
    @Child private SchemeExpression valueToStore_;

    private DefineExprNodeGen(SchemeExpression valueToStore, SchemeSymbol identifier) {
        this.identifier = identifier;
        this.valueToStore_ = valueToStore;
    }

    @Override
    protected SchemeSymbol getIdentifier() {
        return this.identifier;
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        Object valueToStoreValue_ = this.valueToStore_.executeGeneric(frameValue);
        return write(frameValue, valueToStoreValue_);
    }

    @Override
    public NodeCost getCost() {
        return NodeCost.MONOMORPHIC;
    }

    public static DefineExprNode create(SchemeExpression valueToStore, SchemeSymbol identifier) {
        return new DefineExprNodeGen(valueToStore, identifier);
    }

}
