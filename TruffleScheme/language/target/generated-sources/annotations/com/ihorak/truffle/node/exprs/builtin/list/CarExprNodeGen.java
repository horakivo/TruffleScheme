// CheckStyle: start generated
package com.ihorak.truffle.node.exprs.builtin.list;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeCell;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;

@GeneratedBy(CarExprNode.class)
public final class CarExprNodeGen extends CarExprNode {

    @Child private SchemeExpression list_;
    @CompilationFinal private int state_0_;

    private CarExprNodeGen(SchemeExpression list) {
        this.list_ = list;
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        int state_0 = this.state_0_;
        Object listValue_ = this.list_.executeGeneric(frameValue);
        if (state_0 != 0 /* is-state_0 doCar(SchemeCell) */ && listValue_ instanceof SchemeCell) {
            SchemeCell listValue__ = (SchemeCell) listValue_;
            return doCar(listValue__);
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(listValue_);
    }

    private Object executeAndSpecialize(Object listValue) {
        int state_0 = this.state_0_;
        if (listValue instanceof SchemeCell) {
            SchemeCell listValue_ = (SchemeCell) listValue;
            this.state_0_ = state_0 = state_0 | 0b1 /* add-state_0 doCar(SchemeCell) */;
            return doCar(listValue_);
        }
        throw new UnsupportedSpecializationException(this, new Node[] {this.list_}, listValue);
    }

    @Override
    public NodeCost getCost() {
        int state_0 = this.state_0_;
        if (state_0 == 0) {
            return NodeCost.UNINITIALIZED;
        } else {
            return NodeCost.MONOMORPHIC;
        }
    }

    public static CarExprNode create(SchemeExpression list) {
        return new CarExprNodeGen(list);
    }

}
