package com.ihorak.truffle.node.literals;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.frame.VirtualFrame;

import java.util.Objects;

public class LongLiteralNode extends SchemeExpression {

    private final long value;

    public LongLiteralNode(long value) {
        this.value = value;
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        return value;
    }

    @Override
    public long executeLong(VirtualFrame virtualFrame) {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongLiteralNode that = (LongLiteralNode) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
