package com.ihorak.truffle;

import java.io.IOException;

import com.ihorak.truffle.parser.parser.AntlrToAST;
import org.antlr.v4.runtime.CharStreams;

import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeRootNode;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.ContextThreadLocal;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.nodes.Node;

@TruffleLanguage.Registration(id = "scm", name = "Scheme")
public class SchemeTruffleLanguage extends TruffleLanguage<SchemeLanguageContext> {

    private static final LanguageReference<SchemeTruffleLanguage> REFERENCE =
            LanguageReference.create(SchemeTruffleLanguage.class);

    public static SchemeTruffleLanguage get(Node node) {
        return REFERENCE.get(node);
    }

    final ContextThreadLocal<TCOTarget> target = createContextThreadLocal((c, t) -> new TCOTarget());

    @Override
    protected CallTarget parse(ParsingRequest request) throws IOException {
        var globalContext = new ParsingContext(this);
        var charStream = CharStreams.fromReader(request.getSource().getReader());
        var schemeExprs = AntlrToAST.convert(charStream, globalContext);
        var rootNode = new SchemeRootNode(this, globalContext.buildAndGetFrameDescriptor(), schemeExprs);
        return rootNode.getCallTarget();
    }

    @Override
    protected SchemeLanguageContext createContext(Env env) {
        return new SchemeLanguageContext(this);
    }

    public static TCOTarget getTCOTarget(Node node) {
        return REFERENCE.get(node).target.get();
    }

    public static class TCOTarget {

        public CallTarget target;
        public Object[] arguments;

    }

}
