// CheckStyle: start generated
package com.ihorak.truffle.node.special_form.lambda;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;
import com.ihorak.truffle.type.SchemeTypes;
import com.ihorak.truffle.type.SchemeTypesGen;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import java.util.concurrent.locks.Lock;

@GeneratedBy(WriteLocalVariableExprNode.class)
public final class WriteLocalVariableExprNodeGen extends WriteLocalVariableExprNode {

    private final int frameIndex;
    private final SchemeSymbol name;
    @Child private SchemeExpression valueToStore_;
    @CompilationFinal private int state_0_;
    @CompilationFinal private int exclude_;

    private WriteLocalVariableExprNodeGen(SchemeExpression valueToStore, int frameIndex, SchemeSymbol name) {
        this.frameIndex = frameIndex;
        this.name = name;
        this.valueToStore_ = valueToStore;
    }

    @Override
    protected int getFrameIndex() {
        return this.frameIndex;
    }

    @Override
    protected SchemeSymbol getName() {
        return this.name;
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        int state_0 = this.state_0_;
        if ((state_0 & 0b1110) == 0 /* only-active writeLong(VirtualFrame, long) */ && ((state_0 & 0b1111) != 0  /* is-not writeLong(VirtualFrame, long) && writeBoolean(VirtualFrame, boolean) && writeDouble(VirtualFrame, double) && write(VirtualFrame, Object) */)) {
            return executeGeneric_long0(state_0, frameValue);
        } else if ((state_0 & 0b1101) == 0 /* only-active writeBoolean(VirtualFrame, boolean) */ && ((state_0 & 0b1111) != 0  /* is-not writeLong(VirtualFrame, long) && writeBoolean(VirtualFrame, boolean) && writeDouble(VirtualFrame, double) && write(VirtualFrame, Object) */)) {
            return executeGeneric_boolean1(state_0, frameValue);
        } else if ((state_0 & 0b1011) == 0 /* only-active writeDouble(VirtualFrame, double) */ && ((state_0 & 0b1111) != 0  /* is-not writeLong(VirtualFrame, long) && writeBoolean(VirtualFrame, boolean) && writeDouble(VirtualFrame, double) && write(VirtualFrame, Object) */)) {
            return executeGeneric_double2(state_0, frameValue);
        } else {
            return executeGeneric_generic3(state_0, frameValue);
        }
    }

    private Object executeGeneric_long0(int state_0, VirtualFrame frameValue) {
        long valueToStoreValue_;
        try {
            valueToStoreValue_ = this.valueToStore_.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            return executeAndSpecialize(frameValue, ex.getResult());
        }
        assert (state_0 & 0b1) != 0 /* is-state_0 writeLong(VirtualFrame, long) */;
        return writeLong(frameValue, valueToStoreValue_);
    }

    private Object executeGeneric_boolean1(int state_0, VirtualFrame frameValue) {
        boolean valueToStoreValue_;
        try {
            valueToStoreValue_ = this.valueToStore_.executeBoolean(frameValue);
        } catch (UnexpectedResultException ex) {
            return executeAndSpecialize(frameValue, ex.getResult());
        }
        assert (state_0 & 0b10) != 0 /* is-state_0 writeBoolean(VirtualFrame, boolean) */;
        return writeBoolean(frameValue, valueToStoreValue_);
    }

    private Object executeGeneric_double2(int state_0, VirtualFrame frameValue) {
        long valueToStoreValue_long = 0L;
        double valueToStoreValue_;
        try {
            if ((state_0 & 0b100000) == 0 /* only-active 0:double */ && ((state_0 & 0b1111) != 0  /* is-not writeLong(VirtualFrame, long) && writeBoolean(VirtualFrame, boolean) && writeDouble(VirtualFrame, double) && write(VirtualFrame, Object) */)) {
                valueToStoreValue_ = this.valueToStore_.executeDouble(frameValue);
            } else if ((state_0 & 0b10000) == 0 /* only-active 0:double */ && ((state_0 & 0b1111) != 0  /* is-not writeLong(VirtualFrame, long) && writeBoolean(VirtualFrame, boolean) && writeDouble(VirtualFrame, double) && write(VirtualFrame, Object) */)) {
                valueToStoreValue_long = this.valueToStore_.executeLong(frameValue);
                valueToStoreValue_ = SchemeTypes.convertLongToDouble(valueToStoreValue_long);
            } else {
                Object valueToStoreValue__ = this.valueToStore_.executeGeneric(frameValue);
                valueToStoreValue_ = SchemeTypesGen.expectImplicitDouble((state_0 & 0b110000) >>> 4 /* extract-implicit-state_0 0:double */, valueToStoreValue__);
            }
        } catch (UnexpectedResultException ex) {
            return executeAndSpecialize(frameValue, ex.getResult());
        }
        assert (state_0 & 0b100) != 0 /* is-state_0 writeDouble(VirtualFrame, double) */;
        return writeDouble(frameValue, valueToStoreValue_);
    }

    private Object executeGeneric_generic3(int state_0, VirtualFrame frameValue) {
        Object valueToStoreValue_ = this.valueToStore_.executeGeneric(frameValue);
        if ((state_0 & 0b1) != 0 /* is-state_0 writeLong(VirtualFrame, long) */ && valueToStoreValue_ instanceof Long) {
            long valueToStoreValue__ = (long) valueToStoreValue_;
            return writeLong(frameValue, valueToStoreValue__);
        }
        if ((state_0 & 0b10) != 0 /* is-state_0 writeBoolean(VirtualFrame, boolean) */ && valueToStoreValue_ instanceof Boolean) {
            boolean valueToStoreValue__ = (boolean) valueToStoreValue_;
            return writeBoolean(frameValue, valueToStoreValue__);
        }
        if ((state_0 & 0b100) != 0 /* is-state_0 writeDouble(VirtualFrame, double) */ && SchemeTypesGen.isImplicitDouble((state_0 & 0b110000) >>> 4 /* extract-implicit-state_0 0:double */, valueToStoreValue_)) {
            double valueToStoreValue__ = SchemeTypesGen.asImplicitDouble((state_0 & 0b110000) >>> 4 /* extract-implicit-state_0 0:double */, valueToStoreValue_);
            return writeDouble(frameValue, valueToStoreValue__);
        }
        if ((state_0 & 0b1000) != 0 /* is-state_0 write(VirtualFrame, Object) */) {
            return write(frameValue, valueToStoreValue_);
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(frameValue, valueToStoreValue_);
    }

    private Object executeAndSpecialize(VirtualFrame frameValue, Object valueToStoreValue) {
        Lock lock = getLock();
        boolean hasLock = true;
        lock.lock();
        try {
            int state_0 = this.state_0_;
            int exclude = this.exclude_;
            if (((exclude & 0b1)) == 0 /* is-not-exclude writeLong(VirtualFrame, long) */ && valueToStoreValue instanceof Long) {
                long valueToStoreValue_ = (long) valueToStoreValue;
                this.state_0_ = state_0 = state_0 | 0b1 /* add-state_0 writeLong(VirtualFrame, long) */;
                lock.unlock();
                hasLock = false;
                return writeLong(frameValue, valueToStoreValue_);
            }
            if (((exclude & 0b10)) == 0 /* is-not-exclude writeBoolean(VirtualFrame, boolean) */ && valueToStoreValue instanceof Boolean) {
                boolean valueToStoreValue_ = (boolean) valueToStoreValue;
                this.state_0_ = state_0 = state_0 | 0b10 /* add-state_0 writeBoolean(VirtualFrame, boolean) */;
                lock.unlock();
                hasLock = false;
                return writeBoolean(frameValue, valueToStoreValue_);
            }
            if (((exclude & 0b100)) == 0 /* is-not-exclude writeDouble(VirtualFrame, double) */) {
                int doubleCast0;
                if ((doubleCast0 = SchemeTypesGen.specializeImplicitDouble(valueToStoreValue)) != 0) {
                    double valueToStoreValue_ = SchemeTypesGen.asImplicitDouble(doubleCast0, valueToStoreValue);
                    state_0 = (state_0 | (doubleCast0 << 4) /* set-implicit-state_0 0:double */);
                    this.state_0_ = state_0 = state_0 | 0b100 /* add-state_0 writeDouble(VirtualFrame, double) */;
                    lock.unlock();
                    hasLock = false;
                    return writeDouble(frameValue, valueToStoreValue_);
                }
            }
            this.exclude_ = exclude = exclude | 0b111 /* add-exclude writeLong(VirtualFrame, long), writeBoolean(VirtualFrame, boolean), writeDouble(VirtualFrame, double) */;
            state_0 = state_0 & 0xfffffff8 /* remove-state_0 writeLong(VirtualFrame, long), writeBoolean(VirtualFrame, boolean), writeDouble(VirtualFrame, double) */;
            this.state_0_ = state_0 = state_0 | 0b1000 /* add-state_0 write(VirtualFrame, Object) */;
            lock.unlock();
            hasLock = false;
            return write(frameValue, valueToStoreValue);
        } finally {
            if (hasLock) {
                lock.unlock();
            }
        }
    }

    @Override
    public NodeCost getCost() {
        int state_0 = this.state_0_;
        if ((state_0 & 0b1111) == 0) {
            return NodeCost.UNINITIALIZED;
        } else {
            if (((state_0 & 0b1111) & ((state_0 & 0b1111) - 1)) == 0 /* is-single-state_0  */) {
                return NodeCost.MONOMORPHIC;
            }
        }
        return NodeCost.POLYMORPHIC;
    }

    public static WriteLocalVariableExprNode create(SchemeExpression valueToStore, int frameIndex, SchemeSymbol name) {
        return new WriteLocalVariableExprNodeGen(valueToStore, frameIndex, name);
    }

}
