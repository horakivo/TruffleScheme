// CheckStyle: start generated
package com.ihorak.truffle.node;

import com.ihorak.truffle.type.SchemeFunction;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.memory.MemoryFence;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.IndirectCallNode;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import java.util.concurrent.locks.Lock;

@GeneratedBy(ProcedureDispatchNode.class)
public final class ProcedureDispatchNodeGen extends ProcedureDispatchNode {

    @CompilationFinal private volatile int state_0_;
    @CompilationFinal private volatile int exclude_;
    @Child private DirectlyDispatchData directlyDispatch_cache;
    @Child private IndirectCallNode indirectlyDispatch_indirectCallNode_;

    private ProcedureDispatchNodeGen() {
    }

    @ExplodeLoop
    @Override
    public Object executeDispatch(SchemeFunction arg0Value, Object[] arg1Value) {
        int state_0 = this.state_0_;
        if (state_0 != 0 /* is-state_0 directlyDispatch(SchemeFunction, Object[], DirectCallNode) || indirectlyDispatch(SchemeFunction, Object[], IndirectCallNode) */) {
            if ((state_0 & 0b1) != 0 /* is-state_0 directlyDispatch(SchemeFunction, Object[], DirectCallNode) */) {
                DirectlyDispatchData s0_ = this.directlyDispatch_cache;
                while (s0_ != null) {
                    if ((arg0Value.getCallTarget() == s0_.directCallNode_.getCallTarget())) {
                        return ProcedureDispatchNode.directlyDispatch(arg0Value, arg1Value, s0_.directCallNode_);
                    }
                    s0_ = s0_.next_;
                }
            }
            if ((state_0 & 0b10) != 0 /* is-state_0 indirectlyDispatch(SchemeFunction, Object[], IndirectCallNode) */) {
                return ProcedureDispatchNode.indirectlyDispatch(arg0Value, arg1Value, this.indirectlyDispatch_indirectCallNode_);
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(arg0Value, arg1Value);
    }

    private Object executeAndSpecialize(SchemeFunction arg0Value, Object[] arg1Value) {
        Lock lock = getLock();
        boolean hasLock = true;
        lock.lock();
        try {
            int state_0 = this.state_0_;
            int exclude = this.exclude_;
            if ((exclude) == 0 /* is-not-exclude directlyDispatch(SchemeFunction, Object[], DirectCallNode) */) {
                int count0_ = 0;
                DirectlyDispatchData s0_ = this.directlyDispatch_cache;
                if ((state_0 & 0b1) != 0 /* is-state_0 directlyDispatch(SchemeFunction, Object[], DirectCallNode) */) {
                    while (s0_ != null) {
                        if ((arg0Value.getCallTarget() == s0_.directCallNode_.getCallTarget())) {
                            break;
                        }
                        s0_ = s0_.next_;
                        count0_++;
                    }
                }
                if (s0_ == null) {
                    {
                        DirectCallNode directCallNode__ = super.insert((DirectCallNode.create(arg0Value.getCallTarget())));
                        if ((arg0Value.getCallTarget() == directCallNode__.getCallTarget()) && count0_ < (2)) {
                            s0_ = super.insert(new DirectlyDispatchData(directlyDispatch_cache));
                            s0_.directCallNode_ = s0_.insertAccessor(directCallNode__);
                            MemoryFence.storeStore();
                            this.directlyDispatch_cache = s0_;
                            this.state_0_ = state_0 = state_0 | 0b1 /* add-state_0 directlyDispatch(SchemeFunction, Object[], DirectCallNode) */;
                        }
                    }
                }
                if (s0_ != null) {
                    lock.unlock();
                    hasLock = false;
                    return ProcedureDispatchNode.directlyDispatch(arg0Value, arg1Value, s0_.directCallNode_);
                }
            }
            this.indirectlyDispatch_indirectCallNode_ = super.insert((IndirectCallNode.create()));
            this.exclude_ = exclude = exclude | 0b1 /* add-exclude directlyDispatch(SchemeFunction, Object[], DirectCallNode) */;
            this.directlyDispatch_cache = null;
            state_0 = state_0 & 0xfffffffe /* remove-state_0 directlyDispatch(SchemeFunction, Object[], DirectCallNode) */;
            this.state_0_ = state_0 = state_0 | 0b10 /* add-state_0 indirectlyDispatch(SchemeFunction, Object[], IndirectCallNode) */;
            lock.unlock();
            hasLock = false;
            return ProcedureDispatchNode.indirectlyDispatch(arg0Value, arg1Value, this.indirectlyDispatch_indirectCallNode_);
        } finally {
            if (hasLock) {
                lock.unlock();
            }
        }
    }

    @Override
    public NodeCost getCost() {
        int state_0 = this.state_0_;
        if (state_0 == 0) {
            return NodeCost.UNINITIALIZED;
        } else {
            if ((state_0 & (state_0 - 1)) == 0 /* is-single-state_0  */) {
                DirectlyDispatchData s0_ = this.directlyDispatch_cache;
                if ((s0_ == null || s0_.next_ == null)) {
                    return NodeCost.MONOMORPHIC;
                }
            }
        }
        return NodeCost.POLYMORPHIC;
    }

    public static ProcedureDispatchNode create() {
        return new ProcedureDispatchNodeGen();
    }

    @GeneratedBy(ProcedureDispatchNode.class)
    private static final class DirectlyDispatchData extends Node {

        @Child DirectlyDispatchData next_;
        @Child DirectCallNode directCallNode_;

        DirectlyDispatchData(DirectlyDispatchData next_) {
            this.next_ = next_;
        }

        @Override
        public NodeCost getCost() {
            return NodeCost.NONE;
        }

        <T extends Node> T insertAccessor(T node) {
            return super.insert(node);
        }

    }
}
