package com.ihorak.truffle.type;


import java.util.Objects;

public class SchemeSymbol {

    private final String value;

    public SchemeSymbol(String name) {
        this.value = name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "'" + this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchemeSymbol that = (SchemeSymbol) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
