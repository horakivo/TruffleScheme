package com.ihorak.truffle.node.exprs.shared;

import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.node.exprs.LimitedBuiltin;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.UserDefinedProcedure;
import com.ihorak.truffle.type.SchemePair;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;

public abstract class EvalExprNode extends LimitedBuiltin {

//    @Specialization
//    public long evalLong(long value) {
//        return value;
//    }
//
//    @Specialization
//    public boolean evalBoolean(boolean value) {
//        return value;
//    }
//
//    @Specialization
//    public UserDefinedProcedure evalFunction(UserDefinedProcedure value) {
//        return value;
//    }
//
//    @Specialization
//    public Object evalSymbol(VirtualFrame frame, SchemeSymbol value) {
//        return InternalRepresentationConverter.convert(value, createRuntimeContext(), false).executeGeneric(frame);
//    }
//
//    @Specialization
//    public Object evalList(VirtualFrame frame, SchemeCell schemeCell) {
//        return InternalRepresentationConverter.convert(schemeCell, createRuntimeContext(), false).executeGeneric(frame);
//    }
//
//    @Specialization
//    public Object evalPair(SchemePair pair) {
//        throw new SchemeException("eval: cannot evaluate pair", this);
//    }
//
//    //TODO in the future maybe add Mode directly to constructor, right now I would be big effort to change
//    //TODO all the tests if I am not sure if this impl will stay
//    private ParsingContext createRuntimeContext() {
//        //TODO
//        var context = new ParsingContext(null, null);
//        //context.setMode(Mode.RUN_TIME);
//
//        return context;
//    }
}
