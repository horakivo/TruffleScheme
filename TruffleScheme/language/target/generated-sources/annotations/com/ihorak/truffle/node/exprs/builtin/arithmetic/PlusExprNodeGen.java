// CheckStyle: start generated
package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.node.EvalArgumentsNode;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;

@GeneratedBy(PlusExprNode.class)
public final class PlusExprNodeGen extends PlusExprNode {

    @Child private EvalArgumentsNode arguments_;
    @CompilationFinal private int state_0_;

    private PlusExprNodeGen(EvalArgumentsNode arguments) {
        this.arguments_ = arguments;
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        int state_0 = this.state_0_;
        Object argumentsValue_ = this.arguments_.executeGeneric(frameValue);
        if (state_0 != 0 /* is-state_0 addAnyNumberOfLongs(long[]) */ && argumentsValue_ instanceof long[]) {
            long[] argumentsValue__ = (long[]) argumentsValue_;
            return addAnyNumberOfLongs(argumentsValue__);
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(argumentsValue_);
    }

    @Override
    public long executeLong(VirtualFrame frameValue) {
        int state_0 = this.state_0_;
        Object argumentsValue_ = this.arguments_.executeGeneric(frameValue);
        if (state_0 != 0 /* is-state_0 addAnyNumberOfLongs(long[]) */ && argumentsValue_ instanceof long[]) {
            long[] argumentsValue__ = (long[]) argumentsValue_;
            return addAnyNumberOfLongs(argumentsValue__);
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(argumentsValue_);
    }

    private long executeAndSpecialize(Object argumentsValue) {
        int state_0 = this.state_0_;
        if (argumentsValue instanceof long[]) {
            long[] argumentsValue_ = (long[]) argumentsValue;
            this.state_0_ = state_0 = state_0 | 0b1 /* add-state_0 addAnyNumberOfLongs(long[]) */;
            return addAnyNumberOfLongs(argumentsValue_);
        }
        throw new UnsupportedSpecializationException(this, new Node[] {this.arguments_}, argumentsValue);
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

    public static PlusExprNode create(EvalArgumentsNode arguments) {
        return new PlusExprNodeGen(arguments);
    }

}
