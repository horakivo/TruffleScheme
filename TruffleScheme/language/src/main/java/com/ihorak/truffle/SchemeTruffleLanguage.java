package com.ihorak.truffle;

import java.io.IOException;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.convertor.context.ConverterContext;
import com.ihorak.truffle.instruments.GlobalScopeAccess;
import com.ihorak.truffle.parser.AntlrToAST;
import com.ihorak.truffle.runtime.SchemeSymbol;
import com.oracle.truffle.api.strings.TruffleString;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import org.antlr.v4.runtime.CharStreams;

import com.ihorak.truffle.node.SchemeRootNode;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.nodes.Node;

import static com.ihorak.truffle.instruments.GlobalScopeAccessInstrument.GLOBAL_SCOPE_INSTRUMENT_ID;

@TruffleLanguage.Registration(id = "scm", name = "Scheme")
public class SchemeTruffleLanguage extends TruffleLanguage<SchemeLanguageContext> {

    public static final TruffleString.Encoding STRING_ENCODING = TruffleString.Encoding.UTF_16;

    private static final LanguageReference<SchemeTruffleLanguage> REFERENCE =
            LanguageReference.create(SchemeTruffleLanguage.class);

    public static SchemeTruffleLanguage get(Node node) {
        return REFERENCE.get(node);
    }

    @TruffleBoundary
    public Object getGlobalScope(Env env, String languageId) {
        var languageInfo = env.getInternalLanguages().get(languageId);
        var globalScopeAccess = env.lookup(env.getInstruments().get(GLOBAL_SCOPE_INSTRUMENT_ID), GlobalScopeAccess.class);
        return globalScopeAccess.getGlobalScope(languageInfo);
    }

//    final ContextThreadLocal<TCOTarget> target = createContextThreadLocal((c, t) -> new TCOTarget());

    @Override
    protected CallTarget parse(ParsingRequest request) throws IOException {
        var source = request.getSource();
        var globalContext = ConverterContext.createGlobalParsingContext(this, source);
        var charStream = CharStreams.fromReader(request.getSource().getReader());
        var schemeExprs = AntlrToAST.convert(charStream, globalContext);
        var sourceSection = SourceSectionUtil.createSourceSection(schemeExprs, source);
        var rootNode = new SchemeRootNode(this, globalContext.getFrameDescriptorBuilder().build(), schemeExprs, new SchemeSymbol("ROOT"), sourceSection);
        return rootNode.getCallTarget();
    }

    @Override
    protected SchemeLanguageContext createContext(Env env) {
        return new SchemeLanguageContext(env);
    }

//    public static TCOTarget getTCOTarget(Node node) {
//        return REFERENCE.get(node).target.get();
//    }
//
//    public static class TCOTarget {
//
//        public CallTarget target;
//        public Object[] arguments;
//    }

}
