package com.ihorak.truffle.node.builtin.core.polyglot;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.builtin.polyglot.ForeignToSchemeNode;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.GenerateUncached;
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

@GenerateUncached
public abstract class EvalSourceCoreNode extends SchemeNode {
    public static final String POLYGLOT_EVAL_SOURCE = "eval-source";

    public abstract Object execute(Object languageId, Object sourceCode);


    @Specialization(guards = {
            "areTruffleStringsEqual(equalNode, languageId, cachedId)",
            "areTruffleStringsEqual(equalNode, sourceCode, cachedSourceCode)"
    }, limit = "2")
    protected Object evalSourceCached(TruffleString languageId,
                                      TruffleString sourceCode,
                                      @Cached("languageId") TruffleString cachedId,
                                      @Cached("sourceCode") TruffleString cachedSourceCode,
                                      @Cached("create(parse(cachedId, cachedSourceCode))") DirectCallNode callNode,
                                      @Cached TruffleString.EqualNode equalNode,
                                      @Cached ForeignToSchemeNode foreignToSchemeNode) {
        var foreign = callNode.call();
        return foreignToSchemeNode.executeConvert(foreign);
    }


    @TruffleBoundary
    @Specialization(replaces = "evalSourceCached")
    protected Object evalSourceUncached(TruffleString id, TruffleString sourceCode,
                                        @Cached ForeignToSchemeNode foreignToSchemeNode) {
        var foreign = parse(id, sourceCode).call();
        return foreignToSchemeNode.executeConvert(foreign);
    }

    @Fallback
    protected Object doThrow(Object languageId, Object sourceCode) {
        if (!(languageId instanceof TruffleString)) {
            throw SchemeException.contractViolation(this, POLYGLOT_EVAL_SOURCE, "string?", languageId);
        } else if (!(sourceCode instanceof TruffleString)) {
            throw SchemeException.contractViolation(this, POLYGLOT_EVAL_SOURCE, "string?", sourceCode);
        }

        throw SchemeException.shouldNotReachHere();
    }

    protected CallTarget parse(TruffleString id, TruffleString sourceCode) {
        final Source source = Source.newBuilder(id.toJavaStringUncached(), sourceCode.toJavaStringUncached(), POLYGLOT_EVAL_SOURCE).build();
        return getContext().parse(source);
    }
}
