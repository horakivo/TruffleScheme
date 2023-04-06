package com.ihorak.truffle.instruments;

import com.oracle.truffle.api.instrumentation.TruffleInstrument;
import com.oracle.truffle.api.nodes.LanguageInfo;
import org.jetbrains.annotations.Nullable;

public class GlobalScopeAccess {

    private final TruffleInstrument.Env env;

    public GlobalScopeAccess(TruffleInstrument.Env env) {
        this.env = env;
    }

    @Nullable
    public final Object getGlobalScope(LanguageInfo languageInfo) {
        return env.getScope(languageInfo);
    }
}
