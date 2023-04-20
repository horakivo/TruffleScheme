package com.ihorak.truffle.node.polyglot;

import com.ihorak.truffle.node.SchemeNode;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.Specialization;

// we are just converting numbers to supported ones
@GenerateUncached
public abstract class ForeignToSchemeNode extends SchemeNode {

    public abstract Object executeConvert(Object value);

    @Specialization
    protected long convertInt(int value) {
        return value;
    }

    @Specialization
    protected long convertLong(long value) {
        return value;
    }

    @Specialization
    protected long convertByte(byte value) {
        return value;
    }

    @Specialization
    protected long convertShort(short value) {
        return value;
    }

    @Specialization
    protected double convertFloat(float value) {
        return value;
    }

    @Fallback
    protected Object convert(Object value) {
        return value;
    }
}
