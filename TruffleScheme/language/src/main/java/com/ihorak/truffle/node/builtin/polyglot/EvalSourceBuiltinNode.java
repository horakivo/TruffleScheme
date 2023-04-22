package com.ihorak.truffle.node.builtin.polyglot;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.builtin.core.polyglot.EvalSourceCoreNode;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

import static com.ihorak.truffle.node.builtin.core.polyglot.EvalSourceCoreNode.POLYGLOT_EVAL_SOURCE;

public abstract class EvalSourceBuiltinNode extends AlwaysInlinableProcedureNode {


    @Specialization(guards = "arguments.length == 2")
    protected Object doEvalSource(Object[] arguments, @Cached EvalSourceCoreNode evalSourceCoreNode) {
        return evalSourceCoreNode.execute(arguments[0], arguments[1]);
    }

    @Fallback
    protected Object doThrow(Object[] arguments) {
        throw SchemeException.arityException(this, POLYGLOT_EVAL_SOURCE, 2, arguments.length);
    }
}
