package com.ihorak.truffle.node.literals;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.frame.VirtualFrame;

import java.util.Objects;

public class BooleanLiteralNode extends SchemeExpression {

    private final boolean bool;

    public BooleanLiteralNode(boolean bool) {
        this.bool = bool;
    }

    @Override
    public boolean executeBoolean(VirtualFrame virtualFrame) {
        return bool;
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        return bool;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BooleanLiteralNode that = (BooleanLiteralNode) o;
        return bool == that.bool;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bool);
    }
}
