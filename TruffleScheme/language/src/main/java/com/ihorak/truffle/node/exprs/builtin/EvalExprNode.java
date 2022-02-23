package com.ihorak.truffle.node.exprs.builtin;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.parser.Context;
import com.ihorak.truffle.parser.ListToExpressionConverter;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeFunction;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeChild(value = "value")
@NodeField(name = "context", type = Context.class)
public abstract class EvalExprNode extends SchemeExpression {

    protected abstract Context getContext();

    @Specialization
    public long evalLong(long value) {
        return value;
    }

    @Specialization
    public boolean evalBoolean(boolean value) {
        return value;
    }

    @Specialization
    public SchemeFunction evalFunction(SchemeFunction value) {
        return value;
    }

    @Specialization
    public Object evalSymbol(VirtualFrame frame, SchemeSymbol value) {
        return ListToExpressionConverter.convert(value, getContext()).executeGeneric(frame);
    }

    @Specialization
    public Object evalList(VirtualFrame frame, SchemeCell schemeCell) {
        var context = getContext();
        context.setMode(Context.Mode.RUN_TIME);
        return ListToExpressionConverter.convert(schemeCell, context).executeGeneric(frame);
    }
}
