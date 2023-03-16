package com.ihorak.truffle.node.special_form.quasiquote;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import java.util.List;

public class QuasiquoteExprNode extends SchemeExpression {

    private final Object datum;
    @Children
    private final SchemeExpression[] unquoteToEval;
    @Children
    private final SchemeExpression[] unquoteSplicingToEval;

    private final SchemeCell[] unquoteToInsert;
    private final UnquoteSplicingInsertInfo[] unquoteSplicingToInsert;

    public QuasiquoteExprNode(Object datum, List<SchemeExpression> unquoteToEval, List<SchemeCell> unquoteToInsert, List<SchemeExpression> unquoteSplicingToEval, List<UnquoteSplicingInsertInfo> unquoteSplicingToInsert) {
        this.datum = datum;
        this.unquoteToEval = unquoteToEval.toArray(SchemeExpression[]::new);
        this.unquoteToInsert = unquoteToInsert.toArray(SchemeCell[]::new);
        this.unquoteSplicingToEval = unquoteSplicingToEval.toArray(SchemeExpression[]::new);
        this.unquoteSplicingToInsert = unquoteSplicingToInsert.toArray(UnquoteSplicingInsertInfo[]::new);
    }


    @Override
    @ExplodeLoop
    public Object executeGeneric(final VirtualFrame frame) {
        for (int i = 0; i < unquoteToEval.length; i++) {
            unquoteToInsert[i].car = unquoteToEval[i].executeGeneric(frame);
        }

        for (int i = 0; i < unquoteSplicingToEval.length; i++) {
            var insertInfo = unquoteSplicingToInsert[i];
            var listToInsert = (SchemeList) unquoteSplicingToEval[i].executeGeneric(frame);
            //todo validate whether it returns list
            var previousCell = insertInfo.previousCell();
            var cellTeReplace = insertInfo.cellToReplace();
            previousCell.cdr = listToInsert.list;
            listToInsert.bindingCell.cdr = cellTeReplace.cdr;

        }

        return datum;
    }
}
