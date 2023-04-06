package com.ihorak.truffle.node.exprs.builtin;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.interop.ForeignToSchemeNode;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.strings.TruffleString;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;


/**
 * Builtin to evaluate source from different supported language.
 * <p>
 * The call target is cached against the language id and the source code, so that if they are the
 * same each time then a direct call will be made to a cached AST, allowing it to be compiled and
 * possibly inlined.
 */


@NodeChild("id")
@NodeChild("sourceCode")
public abstract class EvalSourceExprNode extends SchemeExpression {


    @Specialization(guards = {
            "areTruffleStringsEqual(equalNode, id, cachedId)",
            "areTruffleStringsEqual(equalNode, sourceCode, cachedSourceCode)"
    }, limit = "2")
    protected Object evalSource(TruffleString id,
                                TruffleString sourceCode,
                                @Cached("id") TruffleString cachedId,
                                @Cached("sourceCode") TruffleString cachedSourceCode,
                                @Cached("create(parse(cachedId, cachedSourceCode))") DirectCallNode callNode,
                                @Cached TruffleString.EqualNode equalNode,
                                @Cached ForeignToSchemeNode foreignToSchemeNode) {
        var foreign = callNode.call();
        return foreignToSchemeNode.executeConvert(foreign);
    }


    @TruffleBoundary
    @Specialization(replaces = "evalSource")
    protected Object evalSourceSlowPath(TruffleString id, TruffleString sourceCode,
                                        @Cached ForeignToSchemeNode foreignToSchemeNode) {
        var foreign = parse(id, sourceCode).call();
        return foreignToSchemeNode.executeConvert(foreign);
    }


    protected boolean areTruffleStringsEqual(TruffleString.EqualNode equalNode, TruffleString left, TruffleString right) {
        return equalNode.execute(left, right, SchemeTruffleLanguage.STRING_ENCODING);
    }


    protected CallTarget parse(TruffleString id, TruffleString sourceCode) {
        final Source source = Source.newBuilder(id.toJavaStringUncached(), sourceCode.toJavaStringUncached(), "eval-source").build();
        return getContext().parse(source);
    }
}
