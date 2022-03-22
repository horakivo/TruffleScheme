package com.ihorak.truffle;

import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.SchemeRootNode;
import com.ihorak.truffle.convertor.Convertor;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.nodes.Node;
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
        var defaultPrimitiveProcedures = DefaultPrimitiveProcedures.generate();
        var globalContext = new ParsingContext(this);
        var programExpressions = Convertor.convertToSchemeExpressions(CharStreams.fromReader(request.getSource().getReader()), globalContext);
        List<SchemeExpression> allExpressions = new ArrayList<>();
        allExpressions.addAll(defaultPrimitiveProcedures);
        allExpressions.addAll(programExpressions);
        var rootNode = new SchemeRootNode(this, globalContext.getFrameDescriptor(), allExpressions);
        return rootNode.getCallTarget();
    }

    @Override
    protected SchemeLanguageContext createContext(Env env) {
        return new SchemeLanguageContext();
    }
}
