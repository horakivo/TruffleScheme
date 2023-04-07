package com.ihorak.truffle.node.interop;

import com.ihorak.truffle.node.SchemeNode;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class ForeignToSchemeNode extends SchemeNode {

    public abstract Object executeConvert(Object value);

    @Specialization
    protected long convertByte(byte value) {
        return value;
    }

    @Specialization
    protected long convertByte(int value) {
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
