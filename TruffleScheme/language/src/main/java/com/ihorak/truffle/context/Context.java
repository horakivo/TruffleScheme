package com.ihorak.truffle.context;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.exceptions.ParserException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.special_form.lambda.ReadGlobalVariableExprNode;
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
    private final SchemeTruffleLanguage language;
    private final LexicalScope lexicalScope;
    private Mode mode = Mode.PARSER;
    private final FrameDescriptor.Builder frameDescriptorBuilder;
    private final Map<SchemeSymbol, Integer> map;
    private final List<SchemeExpression> globalVariableExpressions = new ArrayList<>();

    public Context(Context parent, LexicalScope lexicalScope, SchemeTruffleLanguage language, Mode mode) {
        this.lexicalScope = lexicalScope;
        this.language = language;
        this.parent = parent;
        this.frameDescriptorBuilder = FrameDescriptor.newBuilder();
        this.mode = mode;
        map = new HashMap<>();
    }

    public Context(SchemeTruffleLanguage language, Map<SchemeSymbol, Integer> map, FrameDescriptor.Builder frameDescriptorBuilder) {
        this.lexicalScope = LexicalScope.GLOBAL;
        this.map = map;
        this.language = language;
        this.parent = null;
        this.frameDescriptorBuilder = frameDescriptorBuilder;
    }

    /**
     * Returns null when the symbol is not found in any local environment
     * Therefore global variables are not returned, since those are not stored in VirtualFrames
     *
     * */
    @Nullable
    public Pair findClosureSymbol(SchemeSymbol symbol) {
        return findSymbol(this, symbol, 0);
    }



    @Nullable
    private Pair findSymbol(Context context, SchemeSymbol symbol, int depth) {
        if (context.getLexicalScope() == LexicalScope.GLOBAL) {
            return null;
        }

        Integer frameDescriptorIndex = context.map.get(symbol);

        if (frameDescriptorIndex == null) {
            if (context.parent == null) {
                return null;
            }
            return findSymbol(context.parent, symbol, depth + 1);
        }

        return new Pair(frameDescriptorIndex, depth);
    }

    @Nullable
    public Integer findLocalSymbol(SchemeSymbol symbol) {
        return map.get(symbol);
    }

    public int addLocalSymbol(SchemeSymbol symbol) {
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

    public void addGlobalVariableExpression(ReadGlobalVariableExprNode globalVariableExprNode) {
        var globalContext = findGlobalContext();
        globalContext.globalVariableExpressions.add(globalVariableExprNode);
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

    public SchemeTruffleLanguage getLanguage() {
        return language;
    }
}
