package com.ihorak.truffle.convertor.context;

public class LocalVariableInfo {

    private final int index;
    private boolean isNullable;

    public LocalVariableInfo(final int index, final boolean isNullable) {
        this.index = index;
        this.isNullable = isNullable;
    }


    public int getIndex() {
        return index;
    }

    public boolean isNullable() {
        return isNullable;
    }

    public void setNullable(final boolean nullable) {
        isNullable = nullable;
    }
}
