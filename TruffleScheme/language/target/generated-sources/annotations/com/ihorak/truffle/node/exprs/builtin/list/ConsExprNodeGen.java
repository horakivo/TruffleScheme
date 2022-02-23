// CheckStyle: start generated
package com.ihorak.truffle.node.exprs.builtin.list;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeCost;

@GeneratedBy(ConsExprNode.class)
public final class ConsExprNodeGen extends ConsExprNode {

    @Child private SchemeExpression car_;
    @Child private SchemeExpression cdr_;

    private ConsExprNodeGen(SchemeExpression car, SchemeExpression cdr) {
        this.car_ = car;
        this.cdr_ = cdr;
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        Object carValue_ = this.car_.executeGeneric(frameValue);
        Object cdrValue_ = this.cdr_.executeGeneric(frameValue);
        return doCons(carValue_, cdrValue_);
    }

    @Override
    public NodeCost getCost() {
        return NodeCost.MONOMORPHIC;
    }

    public static ConsExprNode create(SchemeExpression car, SchemeExpression cdr) {
        return new ConsExprNodeGen(car, cdr);
    }

}
