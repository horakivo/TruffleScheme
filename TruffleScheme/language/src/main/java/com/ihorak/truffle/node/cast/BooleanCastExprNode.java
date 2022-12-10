package com.ihorak.truffle.node.cast;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemePair;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeChild(value = "value", type = SchemeExpression.class)
public abstract class BooleanCastExprNode extends SchemeNode {


    public abstract boolean executeBoolean(VirtualFrame frame);
    public abstract boolean executeBoolean(Object value);

    @Specialization
    protected boolean doBoolean(boolean value) {
        return value;
    }

    @Specialization
    protected boolean doLong(long value) {
        return true;
    }

    @Specialization
    protected boolean doDouble(double value) {
        return true;
    }

    @Specialization
    protected boolean doSchemeCell(SchemeCell value) {
        return true;
    }

    @Specialization
    protected boolean doSchemePair(SchemePair value) {
        return true;
    }
}
