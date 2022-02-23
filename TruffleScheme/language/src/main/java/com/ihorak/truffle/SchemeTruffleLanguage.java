package com.ihorak.truffle;

import com.ihorak.truffle.node.SchemeRootNode;
import com.ihorak.truffle.parser.Context;
import com.ihorak.truffle.parser.Reader;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.TruffleLanguage;
import org.antlr.v4.runtime.CharStreams;

@TruffleLanguage.Registration(id = "scm", name = "Scheme")
public class SchemeTruffleLanguage extends TruffleLanguage<Void> {

    @Override
    protected CallTarget parse(ParsingRequest request) throws Exception {
        var globalContext = new Context();
        var exprs = Reader.readProgram2(CharStreams.fromReader(request.getSource().getReader()), globalContext);
        var rootNode = new SchemeRootNode(this, globalContext.getFrameDescriptor(), exprs);
        return rootNode.getCallTarget();
    }

    @Override
    protected Void createContext(Env env) {
        return null;
    }
}
