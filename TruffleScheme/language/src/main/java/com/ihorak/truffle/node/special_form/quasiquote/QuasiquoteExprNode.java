package com.ihorak.truffle.node.special_form.quasiquote;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.runtime.SchemeList;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import java.util.List;

public class QuasiquoteExprNode extends SchemeExpression {

    @Child private QuasiquoteOnlyUnquoteExprNode unquoteNode;
    @Child private QuasiquoteOnlyUnquoteSplicingExprNode unquoteSplicingNode;

    public QuasiquoteExprNode(SchemeList datum, List<SchemeExpression> unquoteToEval, List<SchemeList> unquoteToInsert, List<SchemeExpression> unquoteSplicingToEval, List<UnquoteSplicingInsertInfo> unquoteSplicingToInsert, boolean isFirstPreviousCellNull) {
        unquoteNode = new QuasiquoteOnlyUnquoteExprNode(unquoteToEval, unquoteToInsert, datum);
        unquoteSplicingNode = new QuasiquoteOnlyUnquoteSplicingExprNode(datum, unquoteSplicingToEval, unquoteSplicingToInsert, isFirstPreviousCellNull);
    }


    @Override
    @ExplodeLoop
    public Object executeGeneric(final VirtualFrame frame) {
        unquoteNode.executeGeneric(frame);
        return unquoteSplicingNode.executeGeneric(frame);
    }

}
