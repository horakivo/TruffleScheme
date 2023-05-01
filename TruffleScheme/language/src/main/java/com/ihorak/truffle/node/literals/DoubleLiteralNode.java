package com.ihorak.truffle.node.literals;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.frame.VirtualFrame;

import java.util.Objects;

public class DoubleLiteralNode extends SchemeExpression {

    private final double value;

    public DoubleLiteralNode(double value) {
        this.value = value;
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        return value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleLiteralNode that = (DoubleLiteralNode) o;
        return Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
