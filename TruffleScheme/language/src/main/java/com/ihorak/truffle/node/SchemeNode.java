package com.ihorak.truffle.node;

import com.ihorak.truffle.SchemeLanguageContext;
import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.type.SchemeTypes;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.nodes.Node;

@TypeSystemReference(SchemeTypes.class)
public abstract class SchemeNode extends Node {

    protected static final int TCO_ARGUMENT_SLOT = 0;
    protected static final int TCO_CALLTARGET_SLOT = 1;

    protected SchemeLanguageContext getContext() {
        return SchemeLanguageContext.get(this);
    }

    protected SchemeTruffleLanguage getLanguage() {
        return SchemeTruffleLanguage.get(this);
    }

}
