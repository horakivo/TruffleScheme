// CheckStyle: start generated
package com.ihorak.truffle.node.special_form.lambda;

import com.ihorak.truffle.type.SchemeSymbol;
import com.ihorak.truffle.type.SchemeTypesGen;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import java.util.concurrent.locks.Lock;

@GeneratedBy(ReadLocalVariableExprNode.class)
public final class ReadLocalVariableExprNodeGen extends ReadLocalVariableExprNode {

    private final SchemeSymbol symbol;
    private final int frameSlotIndex;
    private final int lexicalScopeDepth;
    @CompilationFinal private int state_0_;
    @CompilationFinal private int exclude_;

    private ReadLocalVariableExprNodeGen(SchemeSymbol symbol, int frameSlotIndex, int lexicalScopeDepth) {
        this.symbol = symbol;
        this.frameSlotIndex = frameSlotIndex;
        this.lexicalScopeDepth = lexicalScopeDepth;
    }

    @Override
    protected SchemeSymbol getSymbol() {
        return this.symbol;
    }

    @Override
    protected int getFrameSlotIndex() {
        return this.frameSlotIndex;
    }

    @Override
    protected int getLexicalScopeDepth() {
        return this.lexicalScopeDepth;
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        int state_0 = this.state_0_;
        if ((state_0 & 0b1) != 0 /* is-state_0 readLong(VirtualFrame) */) {
            if ((isLong(frameValue))) {
                return readLong(frameValue);
            }
        }
        if ((state_0 & 0b10) != 0 /* is-state_0 readBoolean(VirtualFrame) */) {
            if ((isBoolean(frameValue))) {
                return readBoolean(frameValue);
            }
        }
        if ((state_0 & 0b100) != 0 /* is-state_0 readDouble(VirtualFrame) */) {
            if ((isDouble(frameValue))) {
                return readDouble(frameValue);
            }
        }
        if ((state_0 & 0b1000) != 0 /* is-state_0 read(VirtualFrame) */) {
            return read(frameValue);
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(frameValue);
    }

    @Override
    public boolean executeBoolean(VirtualFrame frameValue) throws UnexpectedResultException {
        int state_0 = this.state_0_;
        if ((state_0 & 0b1000) != 0 /* is-state_0 read(VirtualFrame) */) {
            return SchemeTypesGen.expectBoolean(executeGeneric(frameValue));
        }
        if ((state_0 & 0b10) != 0 /* is-state_0 readBoolean(VirtualFrame) */) {
            if ((isBoolean(frameValue))) {
                return readBoolean(frameValue);
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return SchemeTypesGen.expectBoolean(executeAndSpecialize(frameValue));
    }

    @Override
    public double executeDouble(VirtualFrame frameValue) throws UnexpectedResultException {
        int state_0 = this.state_0_;
        if ((state_0 & 0b1000) != 0 /* is-state_0 read(VirtualFrame) */) {
            return SchemeTypesGen.expectDouble(executeGeneric(frameValue));
        }
        if ((state_0 & 0b100) != 0 /* is-state_0 readDouble(VirtualFrame) */) {
            if ((isDouble(frameValue))) {
                return readDouble(frameValue);
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return SchemeTypesGen.expectDouble(executeAndSpecialize(frameValue));
    }

    @Override
    public long executeLong(VirtualFrame frameValue) throws UnexpectedResultException {
        int state_0 = this.state_0_;
        if ((state_0 & 0b1000) != 0 /* is-state_0 read(VirtualFrame) */) {
            return SchemeTypesGen.expectLong(executeGeneric(frameValue));
        }
        if ((state_0 & 0b1) != 0 /* is-state_0 readLong(VirtualFrame) */) {
            if ((isLong(frameValue))) {
                return readLong(frameValue);
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return SchemeTypesGen.expectLong(executeAndSpecialize(frameValue));
    }

    private Object executeAndSpecialize(VirtualFrame frameValue) {
        Lock lock = getLock();
        boolean hasLock = true;
        lock.lock();
        try {
            int state_0 = this.state_0_;
            int exclude = this.exclude_;
            if (((exclude & 0b1)) == 0 /* is-not-exclude readLong(VirtualFrame) */) {
                if ((isLong(frameValue))) {
                    this.state_0_ = state_0 = state_0 | 0b1 /* add-state_0 readLong(VirtualFrame) */;
                    lock.unlock();
                    hasLock = false;
                    return readLong(frameValue);
                }
            }
            if (((exclude & 0b10)) == 0 /* is-not-exclude readBoolean(VirtualFrame) */) {
                if ((isBoolean(frameValue))) {
                    this.state_0_ = state_0 = state_0 | 0b10 /* add-state_0 readBoolean(VirtualFrame) */;
                    lock.unlock();
                    hasLock = false;
                    return readBoolean(frameValue);
                }
            }
            if (((exclude & 0b100)) == 0 /* is-not-exclude readDouble(VirtualFrame) */) {
                if ((isDouble(frameValue))) {
                    this.state_0_ = state_0 = state_0 | 0b100 /* add-state_0 readDouble(VirtualFrame) */;
                    lock.unlock();
                    hasLock = false;
                    return readDouble(frameValue);
                }
            }
            this.exclude_ = exclude = exclude | 0b111 /* add-exclude readLong(VirtualFrame), readBoolean(VirtualFrame), readDouble(VirtualFrame) */;
            state_0 = state_0 & 0xfffffff8 /* remove-state_0 readLong(VirtualFrame), readBoolean(VirtualFrame), readDouble(VirtualFrame) */;
            this.state_0_ = state_0 = state_0 | 0b1000 /* add-state_0 read(VirtualFrame) */;
            lock.unlock();
            hasLock = false;
            return read(frameValue);
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
                return NodeCost.MONOMORPHIC;
            }
        }
        return NodeCost.POLYMORPHIC;
    }

    public static ReadLocalVariableExprNode create(SchemeSymbol symbol, int frameSlotIndex, int lexicalScopeDepth) {
        return new ReadLocalVariableExprNodeGen(symbol, frameSlotIndex, lexicalScopeDepth);
    }

}
