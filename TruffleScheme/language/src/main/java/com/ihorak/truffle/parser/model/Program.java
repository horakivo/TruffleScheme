package com.ihorak.truffle.parser.model;

import java.util.List;

public class Program {

    private List<Object> internalRepresentations;

    public Program(List<Object> internalRepresentations) {
        this.internalRepresentations = internalRepresentations;
    }

    public List<Object> getInternalRepresentations() {
        return internalRepresentations;
    }
}
