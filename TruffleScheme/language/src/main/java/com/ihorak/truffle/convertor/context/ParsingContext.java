package com.ihorak.truffle.convertor.context;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlotKind;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ParsingContext {

    private final Set<SchemeSymbol> macroIndex;
    private final Map<SchemeSymbol, Integer> lambdaParameterIndex;
    private final Map<SchemeSymbol, Integer> localVariableIndex;

    private final ParsingContext parent;
    private final SchemeTruffleLanguage language;
    private final LexicalScope scope;
    private final FrameDescriptor.Builder frameDescriptorBuilder;


    public ParsingContext(ParsingContext parent, LexicalScope lexicalScope) {
        this.frameDescriptorBuilder = FrameDescriptor.newBuilder();
        this.localVariableIndex = new HashMap<>();
        this.lambdaParameterIndex = new HashMap<>();
        this.macroIndex = new HashSet<>();
        this.scope = lexicalScope;
        this.language = parent.language;
        this.parent = parent;
    }

    //For creating LET - Why do we need to even create this Parsing context not just modify lexical scope?
    //The reason is that if LET is defined in global scope, then we would always consider them as global (look what findClosureSymbol works)
    //This is basically achieving that the storage is the same as the parent one
    //Another benefit is that variables defined in LET at global level will be stored in Frame and not in the global context
    public ParsingContext(ParsingContext parent) {
        this.frameDescriptorBuilder = parent.frameDescriptorBuilder;
        this.scope = LexicalScope.LET;
        this.language = parent.language;
        this.parent = parent;
        this.localVariableIndex = parent.localVariableIndex;
        this.lambdaParameterIndex = parent.lambdaParameterIndex;
        this.macroIndex = parent.macroIndex;
    }

    public ParsingContext(SchemeTruffleLanguage language) {
        this.frameDescriptorBuilder = FrameDescriptor.newBuilder();
        this.localVariableIndex = new HashMap<>();
        this.lambdaParameterIndex = new HashMap<>();
        this.scope = LexicalScope.GLOBAL;
        this.language = language;
        this.parent = null;
        this.macroIndex = new HashSet<>();
    }

    /**
     * Returns null when the symbol is not found in any local environment
     * Therefore global variables are not returned, since those are not stored in VirtualFrames
     */
    @Nullable
    public FrameIndexResult findClosureSymbol(SchemeSymbol symbol) {
        return findSymbol(this, symbol, 0);
    }


    @Nullable
    private FrameIndexResult findSymbol(ParsingContext context, SchemeSymbol symbol, int depth) {
        if (context.getLexicalScope() == LexicalScope.GLOBAL || context.parent == null) {
            return null;
        }

        Integer frameDescriptorIndex = context.localVariableIndex.get(symbol);
        //we found local variable
        if (frameDescriptorIndex != null) return new FrameIndexResult(frameDescriptorIndex, false, depth);
        var argumentIndex = context.getLambdaParameterIndex(symbol);
        //we found argument
        if (argumentIndex != null) return new FrameIndexResult(argumentIndex, true, depth);


        //recursive call
        return findSymbol(context.parent, symbol, depth + 1);

    }

    @Nullable
    public Integer findLocalSymbol(SchemeSymbol symbol) {
        return localVariableIndex.get(symbol);
    }

    public int addLocalSymbol(SchemeSymbol schemeSymbol) {
       return localVariableIndex.computeIfAbsent(schemeSymbol, symbol -> frameDescriptorBuilder.addSlot(FrameSlotKind.Illegal, symbol, null));
    }

    public FrameDescriptor buildAndGetFrameDescriptor() {
        return frameDescriptorBuilder.build();
    }

    public FrameDescriptor.Builder getFrameDescriptorBuilder() {
        return frameDescriptorBuilder;
    }

    public LexicalScope getLexicalScope() {
        return scope;
    }

    public SchemeTruffleLanguage getLanguage() {
        return language;
    }

    public void addMacro(SchemeSymbol schemeSymbol) {
        macroIndex.add(schemeSymbol);
    }

    public boolean isMacro(SchemeSymbol schemeSymbol) {
        return isMacro(schemeSymbol, this);
    }

    private boolean isMacro(SchemeSymbol schemeSymbol, ParsingContext parsingContext) {
        if (parsingContext.macroIndex.contains(schemeSymbol)) return true;
        if (parsingContext.scope == LexicalScope.GLOBAL) return false;

        return isMacro(schemeSymbol, parsingContext.parent);
    }

    public void addLambdaParameter(SchemeSymbol name) {
        lambdaParameterIndex.put(name, lambdaParameterIndex.size());
    }

    @Nullable
    public Integer getLambdaParameterIndex(SchemeSymbol name) {
        return lambdaParameterIndex.get(name);
    }

    public int getNumberOfLambdaParameters() {
        return lambdaParameterIndex.size();
    }

    public Map<SchemeSymbol, Integer> getLocalVariableIndex() {
        return localVariableIndex;
    }

    public Map<SchemeSymbol, Integer> getLambdaParameterIndex() {
        return lambdaParameterIndex;
    }

    public Set<SchemeSymbol> getMacroIndex() {
        return macroIndex;
    }
}
