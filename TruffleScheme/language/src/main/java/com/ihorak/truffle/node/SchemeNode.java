package com.ihorak.truffle.node;

import com.ihorak.truffle.SchemeLanguageContext;
import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.type.SchemeTypes;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.nodes.Node;

@TypeSystemReference(SchemeTypes.class)
public abstract class SchemeNode extends Node {

    protected SchemeLanguageContext getContext() {
        return SchemeLanguageContext.get(this);
    }

    protected SchemeTruffleLanguage getLanguage() {
        return SchemeTruffleLanguage.get(this);
    }

}
