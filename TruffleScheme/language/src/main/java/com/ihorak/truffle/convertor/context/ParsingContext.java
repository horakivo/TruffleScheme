package com.ihorak.truffle.convertor.context;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.convertor.ConverterException;
import com.ihorak.truffle.exceptions.InterpreterException;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.runtime.SchemeList;
import com.ihorak.truffle.runtime.SchemeSymbol;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.source.Source;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ParsingContext {


    private final Map<SchemeSymbol, SchemeExpression> macroIndex = new HashMap<>();
    private final Map<SchemeSymbol, LocalVariableInfo> localVariableIndex;
    private final Source source;


    //Method information

    private final SchemeSymbol functionDefinitionName;
    private boolean isDefiningFunctionShadowed = false;
    private boolean isFunctionSelfTailRecursive = false;
    private Integer selfTailRecursionResultIndex;

    private final List<Integer> procedureArgumentSlotIndexes;
    private final ParsingContext parent;
    private final SchemeTruffleLanguage language;
    private final LexicalScope scope;
    private int quasiquoteNestedLevel = 0;
    private final FrameDescriptor.Builder frameDescriptorBuilder;


    // Lambda
    public ParsingContext(ParsingContext parent, LexicalScope lexicalScope, FrameDescriptor.Builder frameDescriptorBuilder, SchemeSymbol functionDefinitionName, Map<SchemeSymbol, LocalVariableInfo> localVariableIndex, List<Integer> procedureArgumentSlotIndexes) {
        this.frameDescriptorBuilder = frameDescriptorBuilder;
        this.scope = lexicalScope;
        this.functionDefinitionName = functionDefinitionName;
        this.procedureArgumentSlotIndexes = procedureArgumentSlotIndexes;
        this.localVariableIndex = localVariableIndex;
        this.language = parent.language;
        this.parent = parent;
        this.source = parent.getSource();
    }

    // Let
    public ParsingContext(ParsingContext parent) {
        this.frameDescriptorBuilder = parent.getFrameDescriptorBuilder();
        this.scope = LexicalScope.LET;
        this.functionDefinitionName = parent.functionDefinitionName;
        this.procedureArgumentSlotIndexes = parent.procedureArgumentSlotIndexes;
        this.localVariableIndex = new HashMap<>();
        this.language = parent.language;
        this.parent = parent;
        this.source = parent.getSource();
    }

    // Global Parsing context
    private ParsingContext(SchemeTruffleLanguage language, Source source) {
        this.frameDescriptorBuilder = FrameDescriptor.newBuilder();
        this.scope = LexicalScope.GLOBAL;
        this.functionDefinitionName = null;
        this.procedureArgumentSlotIndexes = null;
        this.localVariableIndex = new HashMap<>();
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

    public void addMacro(SchemeSymbol schemeSymbol, SchemeExpression transformationProcedureExpr) {
        macroIndex.put(schemeSymbol, transformationProcedureExpr);
    }

    public boolean isMacro(SchemeSymbol schemeSymbol) {
        var macroInfo = getMacroTransformationExpr(schemeSymbol, this);
        return macroInfo != null;
    }

    private SchemeExpression getMacroTransformationExpr(SchemeSymbol schemeSymbol, ParsingContext parsingContext) {
        var transformationExpr = parsingContext.macroIndex.get(schemeSymbol);
        if (transformationExpr != null) return transformationExpr;
        if (parsingContext.scope == LexicalScope.GLOBAL) return null;

        return getMacroTransformationExpr(schemeSymbol, parsingContext.parent);
    }

    /*
     * Should be called only when we know that the macro exists
     */
    @NotNull
    public SchemeExpression getMacroTransformationExpr(SchemeSymbol symbol) {
        var transformationExpr = getMacroTransformationExpr(symbol, this);
        if (transformationExpr != null) return transformationExpr;

        throw ConverterException.shouldNotReachHere();
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

    public Optional<List<Integer>> getFunctionArgumentSlotIndexes() {
        return Optional.ofNullable(procedureArgumentSlotIndexes);
    }

    public boolean isFunctionSelfTailRecursive() {
        return isFunctionSelfTailRecursive;
    }

    public boolean isDefiningFunctionShadowed() {
        return isDefiningFunctionShadowed;
    }

    public void markDefiningFunctionAsShadowed() {
        if (functionDefinitionName == null) {
            throw ConverterException.shouldNotReachHere("Implementation bug. Wanted to mark function as shadowed but functionDefinitionName is null");
        }

        if (isDefiningFunctionShadowed) {
            throw ConverterException.shouldNotReachHere("Implementation bug. Procedure is already mark as shadowed");
        }

        isDefiningFunctionShadowed = true;
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


    public static ParsingContext createGlobalParsingContext(SchemeTruffleLanguage language, Source source) {
        return new ParsingContext(language, source);
    }

    public static ParsingContext createLetContext(ParsingContext parent) {
        return new ParsingContext(parent);
    }

    public static ParsingContext createLambdaContext(ParsingContext parent, SchemeSymbol functionDefinitionName, SchemeList argumentsIR) {
        var frameDescriptorBuilder = FrameDescriptor.newBuilder();
        List<Integer> procedureArgumentSlotIndexes = new ArrayList<>();
        Map<SchemeSymbol, LocalVariableInfo> localVariableIndex = new HashMap<>();
        for (Object objSymbol : argumentsIR) {
            var symbol = (SchemeSymbol) objSymbol;
            var index = frameDescriptorBuilder.addSlot(FrameSlotKind.Illegal, symbol, null);
            var localVariableInfo = new LocalVariableInfo(index, false);

            procedureArgumentSlotIndexes.add(index);
            localVariableIndex.put(symbol, localVariableInfo);
        }

        return new ParsingContext(parent, LexicalScope.LAMBDA, frameDescriptorBuilder, functionDefinitionName, localVariableIndex, procedureArgumentSlotIndexes);
    }
}
