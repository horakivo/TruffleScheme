// CheckStyle: start generated
package com.ihorak.truffle.node.exprs.builtin.arithmetic;

import com.ihorak.truffle.type.SchemeTypesGen;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;

@GeneratedBy(DivideExprNode.class)
public final class DivideExprNodeGen extends DivideExprNode {

    @CompilationFinal private int state_0_;

    private DivideExprNodeGen() {
    }

    @Override
    public Object execute(Object arg0Value, Object arg1Value) {
        int state_0 = this.state_0_;
        if ((state_0 & 0b1) != 0 /* is-state_0 doDouble(double, double) */ && SchemeTypesGen.isImplicitDouble((state_0 & 0b110) >>> 1 /* extract-implicit-state_0 0:double */, arg0Value)) {
            double arg0Value_ = SchemeTypesGen.asImplicitDouble((state_0 & 0b110) >>> 1 /* extract-implicit-state_0 0:double */, arg0Value);
            if (SchemeTypesGen.isImplicitDouble((state_0 & 0b11000) >>> 3 /* extract-implicit-state_0 1:double */, arg1Value)) {
                double arg1Value_ = SchemeTypesGen.asImplicitDouble((state_0 & 0b11000) >>> 3 /* extract-implicit-state_0 1:double */, arg1Value);
                return doDouble(arg0Value_, arg1Value_);
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(arg0Value, arg1Value);
    }

    private double executeAndSpecialize(Object arg0Value, Object arg1Value) {
        int state_0 = this.state_0_;
        {
            int doubleCast0;
            if ((doubleCast0 = SchemeTypesGen.specializeImplicitDouble(arg0Value)) != 0) {
                double arg0Value_ = SchemeTypesGen.asImplicitDouble(doubleCast0, arg0Value);
                int doubleCast1;
                if ((doubleCast1 = SchemeTypesGen.specializeImplicitDouble(arg1Value)) != 0) {
                    double arg1Value_ = SchemeTypesGen.asImplicitDouble(doubleCast1, arg1Value);
                    state_0 = (state_0 | (doubleCast0 << 1) /* set-implicit-state_0 0:double */);
                    state_0 = (state_0 | (doubleCast1 << 3) /* set-implicit-state_0 1:double */);
                    this.state_0_ = state_0 = state_0 | 0b1 /* add-state_0 doDouble(double, double) */;
                    return doDouble(arg0Value_, arg1Value_);
                }
            }
        }
        throw new UnsupportedSpecializationException(this, new Node[] {null, null}, arg0Value, arg1Value);
    }

    @Override
    public NodeCost getCost() {
        int state_0 = this.state_0_;
        if ((state_0 & 0b1) == 0) {
            return NodeCost.UNINITIALIZED;
        } else {
            return NodeCost.MONOMORPHIC;
        }
    }

    public static DivideExprNode create() {
        return new DivideExprNodeGen();
    }

}
