package com.ihorak.truffle.convertor.context;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlotKind;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ParsingContext {

    private final Set<SchemeSymbol> macroIndex = new HashSet<>();
    private final Map<SchemeSymbol, Integer> lambdaParameterIndex = new HashMap<>();
    private final Map<SchemeSymbol, LocalVariableInfo> localVariableIndex = new HashMap<>();

    private boolean isFunctionDefinition = false;
    private SchemeSymbol functionDefinitionName;

    private final ParsingContext parent;
    private final SchemeTruffleLanguage language;
    private final LexicalScope scope;

    private int quasiquoteNestedLevel = 0;
    private final FrameDescriptor.Builder frameDescriptorBuilder;


    public ParsingContext(ParsingContext parent, LexicalScope lexicalScope) {
        this.frameDescriptorBuilder = FrameDescriptor.newBuilder();
        this.scope = lexicalScope;
        this.language = parent.language;
        this.parent = parent;
    }

    //For creating LET - we don't want to create a new FrameDescriptor because we are using the parent one.
    public ParsingContext(ParsingContext parent, LexicalScope lexicalScope, FrameDescriptor.Builder frameDescriptorBuilder) {
        this.frameDescriptorBuilder = frameDescriptorBuilder;
        this.scope = lexicalScope;
        this.language = parent.language;
        this.parent = parent;
    }

    public ParsingContext(SchemeTruffleLanguage language) {
        this.frameDescriptorBuilder = FrameDescriptor.newBuilder();
        this.scope = LexicalScope.GLOBAL;
        this.language = language;
        this.parent = null;
    }

    /**
     * Returns null when the symbol is not found in any local environment
     * Therefore global variables are not returned, since those are not stored in VirtualFrames
     * <p>
     * This function also do LET adjustments. Because we are storing all the LET variable in the same frame, but during the parsing
     * we are creating the environments we need to then subtract the right amount from the lexical depth.
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

        LocalVariableInfo localVariableInfo = context.localVariableIndex.get(symbol);
        //we found local variable
        if (localVariableInfo != null) return new FrameIndexResult(localVariableInfo.getIndex(), false, localVariableInfo.isNullable(), depth);
        var argumentIndex = context.getLambdaParameterIndex(symbol);
        //we found argument
        if (argumentIndex != null) return new FrameIndexResult(argumentIndex, true, false, depth);


        //recursive call
        if (context.scope == LexicalScope.LET || context.scope == LexicalScope.LETREC) {
            return findSymbol(context.parent, symbol, depth);
        }
        return findSymbol(context.parent, symbol, depth + 1);

    }

    public int findOrAddLocalSymbol(SchemeSymbol schemeSymbol) {
        var localInfo = localVariableIndex.computeIfAbsent(schemeSymbol, symbol -> {
            var index = frameDescriptorBuilder.addSlot(FrameSlotKind.Illegal, symbol, null);
            return new LocalVariableInfo(index, false);
        });

        return localInfo.getIndex();
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

    public void makeLocalVariablesNullable(List<SchemeSymbol> names) {
        for (SchemeSymbol name : names) {
            var localVarInfo = localVariableIndex.get(name);
            if (localVarInfo == null) throw new SchemeException("CONVERTER ERROR: Unable to update local variable to nullable type. Name: " + names, null);
            localVarInfo.setNullable(true);
        }
    }

    public void makeLocalVariablesNonNullable(List<SchemeSymbol> names) {
        for (SchemeSymbol name : names) {
            var localVarInfo = localVariableIndex.get(name);
            if (localVarInfo == null) throw new SchemeException("CONVERTER ERROR: Unable to update local variable to non-nullable type. Name: " + names, null);
            localVarInfo.setNullable(false);
        }
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

    public SchemeSymbol getFunctionDefinitionName() {
        return functionDefinitionName;
    }

    public void setFunctionDefinitionName(final SchemeSymbol functionDefinitionName) {
        this.functionDefinitionName = functionDefinitionName;
    }

    public void setFunctionDefinition(final boolean functionDefinition) {
        isFunctionDefinition = functionDefinition;
    }

    public boolean isFunctionDefinition() {
        return isFunctionDefinition;
    }

    public int getQuasiquoteNestedLevel() {
        return quasiquoteNestedLevel;
    }

    public void increaseQuasiquoteNestedLevel() {
        quasiquoteNestedLevel++;
    }

    public void decreaseQuasiquoteNestedLevel() {
        quasiquoteNestedLevel--;
    }
}
