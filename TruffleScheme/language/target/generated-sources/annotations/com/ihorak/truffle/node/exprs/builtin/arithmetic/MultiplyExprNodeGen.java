// CheckStyle: start generated
package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;

@GeneratedBy(MultiplyExprNode.class)
public final class MultiplyExprNodeGen extends MultiplyExprNode {

    @CompilationFinal private int state_0_;

    private MultiplyExprNodeGen() {
    }

    @Override
    public Object execute(Object arg0Value, Object arg1Value) {
        int state_0 = this.state_0_;
        if (state_0 != 0 /* is-state_0 doLong(long, long) */ && arg0Value instanceof Long) {
            long arg0Value_ = (long) arg0Value;
            if (arg1Value instanceof Long) {
                long arg1Value_ = (long) arg1Value;
                return doLong(arg0Value_, arg1Value_);
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(arg0Value, arg1Value);
    }

    private long executeAndSpecialize(Object arg0Value, Object arg1Value) {
        int state_0 = this.state_0_;
        if (arg0Value instanceof Long) {
            long arg0Value_ = (long) arg0Value;
            if (arg1Value instanceof Long) {
                long arg1Value_ = (long) arg1Value;
                this.state_0_ = state_0 = state_0 | 0b1 /* add-state_0 doLong(long, long) */;
                return doLong(arg0Value_, arg1Value_);
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

    public static MultiplyExprNode create() {
        return new MultiplyExprNodeGen();
    }

}
