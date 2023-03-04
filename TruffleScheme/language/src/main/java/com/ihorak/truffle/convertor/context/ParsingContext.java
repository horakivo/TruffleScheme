package com.ihorak.truffle.convertor.context;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.exceptions.InterpreterException;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.source.Source;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ParsingContext {

    private final Set<SchemeSymbol> macroIndex = new HashSet<>();
    private final Map<SchemeSymbol, LocalVariableInfo> localVariableIndex = new HashMap<>();
    private final Set<SchemeSymbol> tailCallProceduresSet = new HashSet<>();

    private final Source source;

    private SchemeSymbol functionDefinitionName;


    // This can be used to determine whether in lambda body self-tail recursion occur
    private Integer selfTailRecursionArgumentIndex;
    private boolean isProcedureTailCall = false;

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
        if (localVariableInfo != null) return new FrameIndexResult(localVariableInfo.getIndex(), localVariableInfo.isNullable(), depth);

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

    public Optional<SchemeSymbol> getFunctionDefinitionName() {
        return Optional.of(functionDefinitionName);
    }

    public void setFunctionDefinitionName(final SchemeSymbol functionDefinitionName) {
        if (this.functionDefinitionName != null) {
            throw InterpreterException.shouldNotReachHere("Converter error: functionDefinitionName should be set only once!");
        }
        this.functionDefinitionName = functionDefinitionName;
    }

    public Optional<Integer> getSelfTailRecursionArgumentIndex() {
        return Optional.ofNullable(selfTailRecursionArgumentIndex);
    }

    public void setSelfTailRecursionArgumentIndex(int selfTailRecursionArgumentIndex) {
        if (this.selfTailRecursionArgumentIndex != null) {
            throw InterpreterException.shouldNotReachHere("Converter error: selfTailRecursionArgumentIndex should be set only once!");
        }
        this.selfTailRecursionArgumentIndex = selfTailRecursionArgumentIndex;
    }

    public boolean isTailCallProcedureBeingDefined() {
        return isProcedureTailCall;
    }

    public void setProcedureTailCall(boolean procedureTailCall) {
        isProcedureTailCall = procedureTailCall;
    }

    public void addTailCallProcedure(SchemeSymbol nameOfProcedure) {
        tailCallProceduresSet.add(nameOfProcedure);
    }

    public boolean isProcedureTailCall(SchemeSymbol name) {
        return isProcedureTailCall(name, this);
    }

    private boolean isProcedureTailCall(SchemeSymbol name, ParsingContext context) {
        if (context == null) return false;
        if (context.tailCallProceduresSet.contains(name)) return true;

        return isProcedureTailCall(name, context.parent);


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
