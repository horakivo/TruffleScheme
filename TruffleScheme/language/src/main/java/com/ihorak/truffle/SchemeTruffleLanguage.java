package com.ihorak.truffle;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.SchemeRootNode;
import com.ihorak.truffle.context.Context;
import com.ihorak.truffle.parser.Reader;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.TruffleLanguage;
import org.antlr.v4.runtime.CharStreams;

import java.util.ArrayList;
import java.util.List;

@TruffleLanguage.Registration(id = "scm", name = "Scheme")
public class SchemeTruffleLanguage extends TruffleLanguage<Void> {

    @Override
    protected CallTarget parse(ParsingRequest request) throws Exception {
        var defaultPrimitiveProcedures = DefaultPrimitiveProcedures.generate();
        var globalContext = new Context(this, defaultPrimitiveProcedures.getMap(), defaultPrimitiveProcedures.getFrameDescriptorBuilder());
        var programExpressions = Reader.readProgram2(CharStreams.fromReader(request.getSource().getReader()), globalContext);
        List<SchemeExpression> allExpressions = new ArrayList<>();
        allExpressions.addAll(defaultPrimitiveProcedures.getWriteExpressions());
        allExpressions.addAll(programExpressions);
        var rootNode = new SchemeRootNode(this, globalContext.getFrameDescriptor(), allExpressions);
        return rootNode.getCallTarget();
    }

    @Override
    protected Void createContext(Env env) {
        return null;
    }
}
