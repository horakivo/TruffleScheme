package com.ihorak.truffle.node.exprs;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.frame.VirtualFrame;

import java.util.List;

/*
 * This class was created because of the grammar. It is representing list of expressions as a sequence
 * For example (lambda (x) (+ 1 x) (+ 2 x)) is converted to "AST" as
 *
 *                  LAMBDA
 *       /       |         |       \
 *      param1  param2 .. paramN   BODY
 *                                   |
 *                                SEQUENCE
 *                               /       \
 *                           procCall1 ... procCallN
 *
 * */

public class SequenceExprNode extends SchemeExpression {

    @Children
    private SchemeExpression[] schemeExpressions;

    public SequenceExprNode(List<SchemeExpression> schemeExpressions) {
        this.schemeExpressions = schemeExpressions.toArray(SchemeExpression[]::new);
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        Object result = null;
        for (SchemeExpression schemeExpression : schemeExpressions) {
            result = schemeExpression.executeGeneric(virtualFrame);
        }

        return result;
    }

    //TODO remove just for now workaround to make lambda work hopefully :(
    public SchemeExpression[] getSchemeExpressions() {
        return schemeExpressions;
    }
}
