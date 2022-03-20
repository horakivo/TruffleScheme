package com.ihorak.truffle.type;

import com.oracle.truffle.api.CallTarget;

public class SchemeMacro {

    private final SchemeFunction transformationProcedure;

    public SchemeMacro(SchemeFunction transformationProcedure) {
        this.transformationProcedure = transformationProcedure;
    }

    public SchemeFunction getTransformationProcedure() {
        return transformationProcedure;
    }
}
