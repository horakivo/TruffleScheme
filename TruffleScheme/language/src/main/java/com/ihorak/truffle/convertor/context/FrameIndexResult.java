package com.ihorak.truffle.convertor.context;

public class FrameIndexResult {

    private final int frameIndex;
    private final int lexicalScopeDepth;

    public FrameIndexResult(int frameIndex, int lexicalScopeDepth) {
        this.frameIndex = frameIndex;
        this.lexicalScopeDepth = lexicalScopeDepth;
    }

    public int getFrameIndex() {
        return frameIndex;
    }

    public int getLexicalScopeDepth() {
        return lexicalScopeDepth;
    }
}
