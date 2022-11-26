package com.ihorak.truffle;

import com.ihorak.truffle.convertor.InternalRepresentationConverter;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.SchemeRootNode;
import com.ihorak.truffle.parser.parser.Parser;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.nodes.Node;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

import java.util.ArrayList;
import java.util.List;

@TruffleLanguage.Registration(id = "scm", name = "Scheme")
public class SchemeTruffleLanguage extends TruffleLanguage<SchemeLanguageContext> {

    private static final LanguageReference<SchemeTruffleLanguage> REFERENCE =
            LanguageReference.create(SchemeTruffleLanguage.class);

    public static SchemeTruffleLanguage get(Node node) {
        return REFERENCE.get(node);
    }

    @Override
    protected CallTarget parse(ParsingRequest request) throws Exception {
        var globalContext = new ParsingContext(this);
        var programExpressions = convertInternalRepresentationToSchemeExpressions(CharStreams.fromReader(request.getSource().getReader()), globalContext);
        var rootNode = new SchemeRootNode(this, globalContext.buildAndGetFrameDescriptor(), programExpressions);
        return rootNode.getCallTarget();
    }

    public static List<SchemeExpression> convertInternalRepresentationToSchemeExpressions(CharStream charStream, ParsingContext globalContext) {

        var internalRepresentation = Parser.parse(charStream);
        List<SchemeExpression> result = new ArrayList<>();
        for (Object obj : internalRepresentation) {
            result.add(InternalRepresentationConverter.convert(obj, globalContext));
        }

        return result;
    }

    @Override
    protected SchemeLanguageContext createContext(Env env) {
        return new SchemeLanguageContext(this);
    }
}
