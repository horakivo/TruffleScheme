package com.ihorak.truffle.convertor.context;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.exceptions.InterpreterException;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.source.Source;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ParsingContext {

    private final Map<SchemeSymbol, MacroInfo> macroIndex = new HashMap<>();
    private final Map<SchemeSymbol, LocalVariableInfo> localVariableIndex = new HashMap<>();

    private final Source source;


    //Method information

    private SchemeSymbol functionDefinitionName;
    private boolean isFunctionSelfTailRecursive = false;
    private Integer selfTailRecursionResultIndex;
    
    private List<Integer> functionArgumentSlotIndexes;
    private final ParsingContext parent;
    private final SchemeTruffleLanguage language;
    private final LexicalScope scope;
    private int quasiquoteNestedLevel = 0;
    private final FrameDescriptor.Builder frameDescriptorBuilder;


    //Any other child Parsing Context
    public ParsingContext(ParsingContext parent, LexicalScope lexicalScope, Source source) {
        this.frameDescriptorBuilder = FrameDescriptor.newBuilder();
        this.scope = lexicalScope;
        this.language = parent.language;
        this.parent = parent;
        this.source = source;
    }

    //For creating LET - we don't want to create a new FrameDescriptor because we are using the parent one.
    public ParsingContext(ParsingContext parent, LexicalScope lexicalScope, FrameDescriptor.Builder frameDescriptorBuilder, Source source) {
        this.frameDescriptorBuilder = frameDescriptorBuilder;
        this.scope = lexicalScope;
        this.language = parent.language;
        this.parent = parent;
        this.source = source;
    }

    //Global
    public ParsingContext(SchemeTruffleLanguage language, Source source) {
        this.frameDescriptorBuilder = FrameDescriptor.newBuilder();
        this.scope = LexicalScope.GLOBAL;
        this.language = language;
        this.source = source;
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
        if (localVariableInfo != null)
            return new FrameIndexResult(localVariableInfo.getIndex(), localVariableInfo.isNullable(), depth);

        //recursive call
        if (context.scope == LexicalScope.LET) {
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

    public void addMacro(SchemeSymbol schemeSymbol, CallTarget transformationProcedure, int amountOfArgs) {
        macroIndex.put(schemeSymbol, new MacroInfo(transformationProcedure, amountOfArgs));
    }

    public boolean isMacro(SchemeSymbol schemeSymbol) {
        var macroInfo = getMacroInfo(schemeSymbol, this);
        return macroInfo != null;
    }

    private MacroInfo getMacroInfo(SchemeSymbol schemeSymbol, ParsingContext parsingContext) {
        var macroInfo = parsingContext.macroIndex.get(schemeSymbol);
        if (macroInfo != null) return macroInfo;
        if (parsingContext.scope == LexicalScope.GLOBAL) return null;

        return getMacroInfo(schemeSymbol, parsingContext.parent);
    }

    /*
     * Should be called only when we know that the macro exists
     */
    @NotNull
    public MacroInfo getMacroTransformationInfo(SchemeSymbol symbol) {
        var macroInfo = getMacroInfo(symbol, this);
        if (macroInfo != null) return macroInfo;

        throw InterpreterException.shouldNotReachHere();
    }

    public void makeLocalVariablesNullable(List<SchemeSymbol> names) {
        for (SchemeSymbol name : names) {
            var localVarInfo = localVariableIndex.get(name);
            if (localVarInfo == null)
                throw new SchemeException("CONVERTER ERROR: Unable to update local variable to nullable type. Name: " + names, null);
            localVarInfo.setNullable(true);
        }
    }

    public void makeLocalVariablesNonNullable(List<SchemeSymbol> names) {
        for (SchemeSymbol name : names) {
            var localVarInfo = localVariableIndex.get(name);
            if (localVarInfo == null)
                throw new SchemeException("CONVERTER ERROR: Unable to update local variable to non-nullable type. Name: " + names, null);
            localVarInfo.setNullable(false);
        }
    }

    public Optional<SchemeSymbol> getFunctionDefinitionName() {
        return Optional.ofNullable(functionDefinitionName);
    }

    public void setFunctionDefinitionName(final SchemeSymbol functionDefinitionName) {
        if (this.functionDefinitionName != null) {
            throw InterpreterException.shouldNotReachHere("Converter error: functionDefinitionName should be set only once!");
        }
        this.functionDefinitionName = functionDefinitionName;
    }

    public List<Integer> getFunctionArgumentSlotIndexes() {
        return functionArgumentSlotIndexes;
    }

    public void setFunctionArgumentSlotIndexes(List<Integer> functionArgumentSlotIndexes) {
        if (this.functionArgumentSlotIndexes != null) {
            throw InterpreterException.shouldNotReachHere("Converter error: functionArgumentSlotIndexes should be set only once!");
        }
        this.functionArgumentSlotIndexes = functionArgumentSlotIndexes;
    }

    public boolean isFunctionSelfTailRecursive() {
        return isFunctionSelfTailRecursive;
    }

    public void setFunctionAsSelfTailRecursive() {
        isFunctionSelfTailRecursive = true;
    }

    public void setSelfTailRecursionResultIndex(int selfTailRecursionResultIndex) {
        if (this.selfTailRecursionResultIndex != null) {
            throw InterpreterException.shouldNotReachHere("Converter error: selfTailRecursionResultIndex should be set only once!");
        }
        this.selfTailRecursionResultIndex = selfTailRecursionResultIndex;
    }

    public Optional<Integer> getSelfTCOResultFrameSlot() {
        return Optional.ofNullable(selfTailRecursionResultIndex);
    }

    public Source getSource() {
        return source;
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
