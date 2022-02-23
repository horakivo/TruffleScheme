package com.ihorak.truffle.parser;

import com.ihorak.truffle.node.literals.SymbolExprNodeGen;
import com.ihorak.truffle.parser.Util.Pair;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlotKind;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Context {

    private Context parent;
    private final LexicalScope lexicalScope;
    private Mode mode = Mode.PARSER;
    private final FrameDescriptor.Builder frameDescriptorBuilder;
    private final Map<SchemeSymbol, Integer> map = new HashMap<>();

    public Context(Context parent, LexicalScope lexicalScope) {
        this.lexicalScope = lexicalScope;
        this.parent = parent;
        this.frameDescriptorBuilder = FrameDescriptor.newBuilder();
    }

    public Context() {
        this.lexicalScope = LexicalScope.GLOBAL;
        this.parent = null;
        this.frameDescriptorBuilder = FrameDescriptor.newBuilder();
    }

    @Nullable
    public Pair findSymbol(SchemeSymbol symbol) {
        return findSymbol(this, symbol, 0);
    }

    @Nullable
    public Integer findLocalSymbol(SchemeSymbol symbol) {
        return map.get(symbol);
    }

    private Pair findSymbol(Context context, SchemeSymbol symbol, int depth) {
        Integer frameDescriptorIndex = context.map.get(symbol);

        if (frameDescriptorIndex == null) {
            if (context.parent == null) {
                return null;
            }
            return findSymbol(context.parent, symbol, depth + 1);
        }

        return new Pair(frameDescriptorIndex, depth);
    }

    public int addLocalSymbol(SchemeSymbol symbol) {
        if (mode == Mode.RUN_TIME) {
            throw new ParserException("Parser: Values shouldn't be added during runtime! Parser mistake");
        }
        Integer frameDescriptorIndex = map.get(symbol);
        if (frameDescriptorIndex == null) {
            int index = frameDescriptorBuilder.addSlot(FrameSlotKind.Illegal, symbol, null);
            map.put(symbol, index);
            return index;
        }
        return frameDescriptorIndex;
    }

    public int addGlobalSymbol(SchemeSymbol symbol) {
        if (mode == Mode.RUN_TIME) {
            throw new ParserException("Parser: Values shouldn't be added during runtime! Parser mistake");
        }

        var globalContext = findGlobalContext();
        var frameDescriptorIndex = globalContext.map.get(symbol);
        if (frameDescriptorIndex == null) {
            var index = globalContext.frameDescriptorBuilder.addSlot(FrameSlotKind.Illegal, symbol, null);
            globalContext.map.put(symbol, index);
            return index;
        }
        return frameDescriptorIndex;
    }

    private Context findGlobalContext() {
        var currentContext = this;
        while (currentContext.parent != null) {
            currentContext = currentContext.parent;
        }

        return currentContext;
    }

    public FrameDescriptor getFrameDescriptor() {
        return frameDescriptorBuilder.build();
    }

    public LexicalScope getLexicalScope() {
        return lexicalScope;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    enum LexicalScope {
        LAMBDA,
        GLOBAL,
        LET
    }

    public enum Mode {
        PARSER,
        RUN_TIME
    }
}
