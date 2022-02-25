package com.ihorak.truffle.node.exprs.builtin;

import com.ihorak.truffle.context.Mode;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.context.Context;
import com.ihorak.truffle.parser.ListToExpressionConverter;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeFunction;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeChild(value = "value")
public abstract class EvalExprNode extends SchemeExpression {

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
        return ListToExpressionConverter.convert(value, createRuntimeContext()).executeGeneric(frame);
    }

    @Specialization
    public Object evalList(VirtualFrame frame, SchemeCell schemeCell) {
        return ListToExpressionConverter.convert(schemeCell, createRuntimeContext()).executeGeneric(frame);
    }

    //TODO in the future maybe add Mode directly to constructor, right now I would be big effort to change
    //TODO all the tests if I am not sure if this impl will stay
    private Context createRuntimeContext() {
        var context = new Context();
        context.setMode(Mode.RUN_TIME);

        return context;
    }
}
