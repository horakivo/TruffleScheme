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

    private final Context parent;
    private final SchemeTruffleLanguage language;
    private final LexicalScope lexicalScope;
    private Mode mode = Mode.PARSER;
    private final FrameDescriptor.Builder frameDescriptorBuilder = FrameDescriptor.newBuilder();
    private final Map<SchemeSymbol, Integer> map = new HashMap<>();

    public Context(Context parent, LexicalScope lexicalScope, SchemeTruffleLanguage language, Mode mode) {
        this.lexicalScope = lexicalScope;
        this.language = language;
        this.parent = parent;
        this.mode = mode;
    }

    public Context(SchemeTruffleLanguage language) {
        this.lexicalScope = LexicalScope.GLOBAL;
        this.language = language;
        this.parent = null;
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
