package com.ihorak.truffle.node.special_form.quasiquote;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeList;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import java.util.List;

public class QuasiquoteOnlyUnquoteSplicingExprNode extends SchemeExpression {

    private final SchemeList datum;

    @Children
    private final SchemeExpression[] unquoteSplicingToEval;

    @CompilationFinal(dimensions = 1)
    private final UnquoteSplicingInsertInfo[] unquoteSplicingToInsert;

    private final boolean isFirstPreviousCellNull;

    public QuasiquoteOnlyUnquoteSplicingExprNode(SchemeList datum, List<SchemeExpression> unquoteSplicingToEval, List<UnquoteSplicingInsertInfo> unquoteSplicingToInsert, boolean isFirstPreviousCellNull) {
        this.datum = datum;
        this.unquoteSplicingToEval = unquoteSplicingToEval.toArray(SchemeExpression[]::new);
        this.unquoteSplicingToInsert = unquoteSplicingToInsert.toArray(UnquoteSplicingInsertInfo[]::new);
        this.isFirstPreviousCellNull = isFirstPreviousCellNull;
    }

    @Override
    @ExplodeLoop
    public Object executeGeneric(VirtualFrame frame) {
        int index = 0;
        if (isFirstPreviousCellNull) {
            var listToInsert = getListAtIndex(0, frame);
            var cellToReplace = unquoteSplicingToInsert[0].cellToReplace();

            var listToInsertBindingCell = findBindingCell(listToInsert);
            listToInsertBindingCell.cdr = cellToReplace.cdr;
            cellToReplace.car = listToInsert.car;
            cellToReplace.cdr = listToInsert.cdr;

            index++;
        }


        for (; index < unquoteSplicingToEval.length; index++) {
            var insertInfo = unquoteSplicingToInsert[index];
            var listToInsert = getListAtIndex(index, frame);

            var previousCell = insertInfo.previousCell();
            var cellTeReplace = insertInfo.cellToReplace();
            previousCell.cdr = listToInsert;
            var listToInsertBindingCell = findBindingCell(listToInsert);
            listToInsertBindingCell.cdr = cellTeReplace.cdr;
        }

        return datum;
    }

    private SchemeList findBindingCell(SchemeList list) {
        var currentList = list;
        while (currentList.cdr != SchemeList.EMPTY_LIST) {
            currentList = currentList.cdr;
        }

        return currentList;
    }


    private SchemeList getListAtIndex(int index, VirtualFrame frame) {
        var object = unquoteSplicingToEval[index].executeGeneric(frame);

        if (!(object instanceof SchemeList list)) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw SchemeException.contractViolation(this, "unquote-splicing", "list?", object);
        }

        return list;
    }
}
