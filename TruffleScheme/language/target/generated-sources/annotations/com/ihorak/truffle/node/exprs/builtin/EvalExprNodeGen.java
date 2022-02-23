// CheckStyle: start generated
package com.ihorak.truffle.node.exprs.builtin;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.parser.Context;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeFunction;
import com.ihorak.truffle.type.SchemeSymbol;
import com.ihorak.truffle.type.SchemeTypesGen;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

@GeneratedBy(EvalExprNode.class)
public final class EvalExprNodeGen extends EvalExprNode {

    private final Context context;
    @Child private SchemeExpression value_;
    @CompilationFinal private int state_0_;

    private EvalExprNodeGen(SchemeExpression value, Context context) {
        this.context = context;
        this.value_ = value;
    }

    @Override
    protected Context getContext() {
        return this.context;
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        int state_0 = this.state_0_;
        if ((state_0 & 0b11110) == 0 /* only-active evalLong(long) */ && (state_0 != 0  /* is-not evalLong(long) && evalBoolean(boolean) && evalFunction(SchemeFunction) && evalSymbol(VirtualFrame, SchemeSymbol) && evalList(VirtualFrame, SchemeCell) */)) {
            return executeGeneric_long0(state_0, frameValue);
        } else if ((state_0 & 0b11101) == 0 /* only-active evalBoolean(boolean) */ && (state_0 != 0  /* is-not evalLong(long) && evalBoolean(boolean) && evalFunction(SchemeFunction) && evalSymbol(VirtualFrame, SchemeSymbol) && evalList(VirtualFrame, SchemeCell) */)) {
            return executeGeneric_boolean1(state_0, frameValue);
        } else {
            return executeGeneric_generic2(state_0, frameValue);
        }
    }

    private Object executeGeneric_long0(int state_0, VirtualFrame frameValue) {
        long valueValue_;
        try {
            valueValue_ = this.value_.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            return executeAndSpecialize(frameValue, ex.getResult());
        }
        assert (state_0 & 0b1) != 0 /* is-state_0 evalLong(long) */;
        return evalLong(valueValue_);
    }

    private Object executeGeneric_boolean1(int state_0, VirtualFrame frameValue) {
        boolean valueValue_;
        try {
            valueValue_ = this.value_.executeBoolean(frameValue);
        } catch (UnexpectedResultException ex) {
            return executeAndSpecialize(frameValue, ex.getResult());
        }
        assert (state_0 & 0b10) != 0 /* is-state_0 evalBoolean(boolean) */;
        return evalBoolean(valueValue_);
    }

    private Object executeGeneric_generic2(int state_0, VirtualFrame frameValue) {
        Object valueValue_ = this.value_.executeGeneric(frameValue);
        if ((state_0 & 0b1) != 0 /* is-state_0 evalLong(long) */ && valueValue_ instanceof Long) {
            long valueValue__ = (long) valueValue_;
            return evalLong(valueValue__);
        }
        if ((state_0 & 0b10) != 0 /* is-state_0 evalBoolean(boolean) */ && valueValue_ instanceof Boolean) {
            boolean valueValue__ = (boolean) valueValue_;
            return evalBoolean(valueValue__);
        }
        if ((state_0 & 0b100) != 0 /* is-state_0 evalFunction(SchemeFunction) */ && valueValue_ instanceof SchemeFunction) {
            SchemeFunction valueValue__ = (SchemeFunction) valueValue_;
            return evalFunction(valueValue__);
        }
        if ((state_0 & 0b1000) != 0 /* is-state_0 evalSymbol(VirtualFrame, SchemeSymbol) */ && valueValue_ instanceof SchemeSymbol) {
            SchemeSymbol valueValue__ = (SchemeSymbol) valueValue_;
            return evalSymbol(frameValue, valueValue__);
        }
        if ((state_0 & 0b10000) != 0 /* is-state_0 evalList(VirtualFrame, SchemeCell) */ && valueValue_ instanceof SchemeCell) {
            SchemeCell valueValue__ = (SchemeCell) valueValue_;
            return evalList(frameValue, valueValue__);
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(frameValue, valueValue_);
    }

    @Override
    public boolean executeBoolean(VirtualFrame frameValue) throws UnexpectedResultException {
        int state_0 = this.state_0_;
        if ((state_0 & 0b11000) != 0 /* is-state_0 evalSymbol(VirtualFrame, SchemeSymbol) || evalList(VirtualFrame, SchemeCell) */) {
            return SchemeTypesGen.expectBoolean(executeGeneric(frameValue));
        }
        boolean valueValue_;
        try {
            valueValue_ = this.value_.executeBoolean(frameValue);
        } catch (UnexpectedResultException ex) {
            return SchemeTypesGen.expectBoolean(executeAndSpecialize(frameValue, ex.getResult()));
        }
        if ((state_0 & 0b10) != 0 /* is-state_0 evalBoolean(boolean) */) {
            return evalBoolean(valueValue_);
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return SchemeTypesGen.expectBoolean(executeAndSpecialize(frameValue, valueValue_));
    }

    @Override
    public long executeLong(VirtualFrame frameValue) throws UnexpectedResultException {
        int state_0 = this.state_0_;
        if ((state_0 & 0b11000) != 0 /* is-state_0 evalSymbol(VirtualFrame, SchemeSymbol) || evalList(VirtualFrame, SchemeCell) */) {
            return SchemeTypesGen.expectLong(executeGeneric(frameValue));
        }
        long valueValue_;
        try {
            valueValue_ = this.value_.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            return SchemeTypesGen.expectLong(executeAndSpecialize(frameValue, ex.getResult()));
        }
        if ((state_0 & 0b1) != 0 /* is-state_0 evalLong(long) */) {
            return evalLong(valueValue_);
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return SchemeTypesGen.expectLong(executeAndSpecialize(frameValue, valueValue_));
    }

    private Object executeAndSpecialize(VirtualFrame frameValue, Object valueValue) {
        int state_0 = this.state_0_;
        if (valueValue instanceof Long) {
            long valueValue_ = (long) valueValue;
            this.state_0_ = state_0 = state_0 | 0b1 /* add-state_0 evalLong(long) */;
            return evalLong(valueValue_);
        }
        if (valueValue instanceof Boolean) {
            boolean valueValue_ = (boolean) valueValue;
            this.state_0_ = state_0 = state_0 | 0b10 /* add-state_0 evalBoolean(boolean) */;
            return evalBoolean(valueValue_);
        }
        if (valueValue instanceof SchemeFunction) {
            SchemeFunction valueValue_ = (SchemeFunction) valueValue;
            this.state_0_ = state_0 = state_0 | 0b100 /* add-state_0 evalFunction(SchemeFunction) */;
            return evalFunction(valueValue_);
        }
        if (valueValue instanceof SchemeSymbol) {
            SchemeSymbol valueValue_ = (SchemeSymbol) valueValue;
            this.state_0_ = state_0 = state_0 | 0b1000 /* add-state_0 evalSymbol(VirtualFrame, SchemeSymbol) */;
            return evalSymbol(frameValue, valueValue_);
        }
        if (valueValue instanceof SchemeCell) {
            SchemeCell valueValue_ = (SchemeCell) valueValue;
            this.state_0_ = state_0 = state_0 | 0b10000 /* add-state_0 evalList(VirtualFrame, SchemeCell) */;
            return evalList(frameValue, valueValue_);
        }
        throw new UnsupportedSpecializationException(this, new Node[] {this.value_}, valueValue);
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

    public static EvalExprNode create(SchemeExpression value, Context context) {
        return new EvalExprNodeGen(value, context);
    }

}
