package com.ihorak.truffle.parser.Util;

public class Pair {

    private final int frameIndex;
    private final int lexicalScopeDepth;

    public Pair(int frameIndex, int lexicalScopeDepth) {
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
