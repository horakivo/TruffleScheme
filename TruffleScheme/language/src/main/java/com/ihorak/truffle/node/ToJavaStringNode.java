package com.ihorak.truffle.node;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.builtin.polyglot.TranslateInteropExceptionNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;

@GenerateUncached
public abstract class ToJavaStringNode extends SchemeNode {

    public abstract String execute(Object value);

    @Specialization(guards = "interop.isString(valueToConvert)", limit = "getInteropCacheLimit()")
    protected String convertToString(Object valueToConvert,
                                     @CachedLibrary("valueToConvert") InteropLibrary interop,
                                     @Cached TranslateInteropExceptionNode translateInteropExceptionNode) {
        try {
            return interop.asString(valueToConvert);
        } catch (InteropException e) {
            throw translateInteropExceptionNode.execute(e, valueToConvert);
        }
    }

    @TruffleBoundary
    @Specialization(guards = "!interop.isString(valueToConvert)", limit = "getInteropCacheLimit()")
    protected String doThrow(Object valueToConvert,
                             @CachedLibrary("valueToConvert") InteropLibrary interop) {
        throw new SchemeException("""
                Interop: contract violation
                expected: symbol? or string?
                given: %s""".formatted(valueToConvert), this);
    }
}
