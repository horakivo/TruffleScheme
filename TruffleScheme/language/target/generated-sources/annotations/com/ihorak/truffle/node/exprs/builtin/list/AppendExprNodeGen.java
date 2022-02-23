// CheckStyle: start generated
package com.ihorak.truffle.node.exprs.builtin.list;

import com.ihorak.truffle.type.SchemeCell;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;

@GeneratedBy(AppendExprNode.class)
public final class AppendExprNodeGen extends AppendExprNode {

    @CompilationFinal private int state_0_;

    private AppendExprNodeGen() {
    }

    @Override
    public Object execute(Object arg0Value, Object arg1Value) {
        int state_0 = this.state_0_;
        if (state_0 != 0 /* is-state_0 doAppend(SchemeCell, SchemeCell) */ && arg0Value instanceof SchemeCell) {
            SchemeCell arg0Value_ = (SchemeCell) arg0Value;
            if (arg1Value instanceof SchemeCell) {
                SchemeCell arg1Value_ = (SchemeCell) arg1Value;
                return doAppend(arg0Value_, arg1Value_);
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(arg0Value, arg1Value);
    }

    private SchemeCell executeAndSpecialize(Object arg0Value, Object arg1Value) {
        int state_0 = this.state_0_;
        if (arg0Value instanceof SchemeCell) {
            SchemeCell arg0Value_ = (SchemeCell) arg0Value;
            if (arg1Value instanceof SchemeCell) {
                SchemeCell arg1Value_ = (SchemeCell) arg1Value;
                this.state_0_ = state_0 = state_0 | 0b1 /* add-state_0 doAppend(SchemeCell, SchemeCell) */;
                return doAppend(arg0Value_, arg1Value_);
            }
        }
        throw new UnsupportedSpecializationException(this, new Node[] {null, null}, arg0Value, arg1Value);
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

    public static AppendExprNode create() {
        return new AppendExprNodeGen();
    }

}
