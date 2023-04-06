package com.ihorak.truffle.instruments;

import com.oracle.truffle.api.instrumentation.TruffleInstrument;

import static com.ihorak.truffle.instruments.GlobalScopeAccessInstrument.GLOBAL_SCOPE_INSTRUMENT_ID;

@TruffleInstrument.Registration(id = GLOBAL_SCOPE_INSTRUMENT_ID, name = "Global Access Scope Instrument", version = "0.1", services = GlobalScopeAccess.class)
public class GlobalScopeAccessInstrument extends TruffleInstrument {

    public static final String GLOBAL_SCOPE_INSTRUMENT_ID = "globalAccessScope";

    @Override
    protected void onCreate(Env env) {
        env.registerService(new GlobalScopeAccess(env));
    }
}
