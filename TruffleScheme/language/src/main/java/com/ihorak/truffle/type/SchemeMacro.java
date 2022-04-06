package com.ihorak.truffle.type;

public class SchemeMacro {

    private final UserDefinedProcedure transformationProcedure;

    public SchemeMacro(UserDefinedProcedure transformationProcedure) {
        this.transformationProcedure = transformationProcedure;
    }

    public UserDefinedProcedure getTransformationProcedure() {
        return transformationProcedure;
    }
}
