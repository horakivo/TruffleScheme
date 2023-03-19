package com.ihorak.truffle.convertor.context;

import com.oracle.truffle.api.CallTarget;

public record MacroInfo(CallTarget callTarget, int amountOfArgs) {
}
