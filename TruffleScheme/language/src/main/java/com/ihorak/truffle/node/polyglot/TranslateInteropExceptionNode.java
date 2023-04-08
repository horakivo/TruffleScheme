package com.ihorak.truffle.node.polyglot;

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

    public abstract PolyglotException execute(InteropException exception, Object receiver, String messageName, Object[] args);


    @Specialization
    protected PolyglotException handle(UnsupportedMessageException exception, Object receiver, String messageName, Object[] args) {
        return PolyglotException.unsupportedMessageException(exception, receiver, messageName, this);
    }

    @Specialization
    protected PolyglotException handle(UnknownIdentifierException exception, Object receiver, String messageName, Object[] args) {
        return PolyglotException.unknownIdentifierException(exception, receiver, this);
    }

    @Specialization
    protected PolyglotException handle(UnsupportedTypeException exception, Object receiver, String messageName, Object[] args) {
        return PolyglotException.unsupportedTypeException(exception, receiver, this);
    }

    @Specialization
    protected PolyglotException handle(ArityException exception, Object receiver, String messageName, Object[] args) {
        return PolyglotException.arityException(exception, receiver, args, this);
    }

    @Specialization
    protected PolyglotException handle(InvalidArrayIndexException exception, Object receiver, String messageName, Object[] args) {
        return PolyglotException.invalidArrayIndexException(exception, receiver, args, messageName, this);
    }

}
