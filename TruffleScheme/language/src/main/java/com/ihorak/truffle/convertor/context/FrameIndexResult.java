package com.ihorak.truffle.convertor.context;

public record FrameIndexResult(
        int index,
        boolean isLambdaParameter,
        int lexicalScopeDepth) {

}
