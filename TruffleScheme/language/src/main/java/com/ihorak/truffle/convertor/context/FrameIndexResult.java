package com.ihorak.truffle.convertor.context;

import com.ihorak.truffle.exceptions.SchemeException;

public record FrameIndexResult(
        int index,
        boolean isNullable,
        int lexicalScopeDepth) {

    public FrameIndexResult {
        if (lexicalScopeDepth < 0) {
            throw new SchemeException("Lexical scope depth is smaller then 0. Converter error! ", null);
        }
    }

}
