package com.ihorak.truffle.node.builtin.polyglot;

import com.ihorak.truffle.node.SchemeNode;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.ArityException;
import com.oracle.truffle.api.interop.InteropException;
import com.oracle.truffle.api.interop.InvalidArrayIndexException;
import com.oracle.truffle.api.interop.UnknownIdentifierException;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.interop.UnsupportedTypeException;

@GenerateUncached
public abstract class TranslateInteropExceptionNode extends SchemeNode {

    public abstract PolyglotException execute(InteropException exception, Object receiver);


    @Specialization
    protected PolyglotException handle(UnsupportedMessageException exception, Object receiver) {
        return PolyglotException.unsupportedMessageException(exception, receiver, this);
    }

    @Specialization
    protected PolyglotException handle(UnknownIdentifierException exception, Object receiver) {
        return PolyglotException.unknownIdentifierException(exception, receiver, this);
    }

    @Specialization
    protected PolyglotException handle(UnsupportedTypeException exception, Object receiver) {
        return PolyglotException.unsupportedTypeException(exception, receiver, this);
    }

    @Specialization
    protected PolyglotException handle(ArityException exception, Object receiver) {
        return PolyglotException.arityException(exception, receiver, this);
    }

    @Specialization
    protected PolyglotException handle(InvalidArrayIndexException exception, Object receiver) {
        return PolyglotException.invalidArrayIndexException(exception, receiver, this);
    }

}
