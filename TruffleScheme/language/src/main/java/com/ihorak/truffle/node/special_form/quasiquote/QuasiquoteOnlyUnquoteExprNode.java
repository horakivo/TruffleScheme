package com.ihorak.truffle.node.special_form.quasiquote;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import java.util.List;

public class QuasiquoteOnlyUnquoteExprNode extends SchemeExpression {

    private final Object listIR;

    @Children
    private final SchemeExpression[] unquoteToEval;

    @CompilationFinal(dimensions = 1)
    private final SchemeCell[] unquoteToInsert;

    public QuasiquoteOnlyUnquoteExprNode(List<SchemeExpression> unquoteToEval, List<SchemeCell> unquoteToInsert, SchemeList listIR) {
        this.unquoteToEval = unquoteToEval.toArray(SchemeExpression[]::new);
        this.unquoteToInsert = unquoteToInsert.toArray(SchemeCell[]::new);
        this.listIR = listIR;
    }

    @Override
    @ExplodeLoop
    public Object executeGeneric(VirtualFrame frame) {
        for (int i = 0; i < unquoteToEval.length; i++) {
            unquoteToInsert[i].car = unquoteToEval[i].executeGeneric(frame);
        }

        //TODO I should probably return new copy
        return listIR;
    }
}
