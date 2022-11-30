package com.ihorak.truffle.node.exprs.builtin;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.LoopNode;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.RepeatingNode;

public class WhileInfiniteExprNode extends SchemeExpression {

    @Child
    private SchemeExpression expr;

    @Child LoopNode loop;
    
    public WhileInfiniteExprNode(final SchemeExpression expr) {
        this.expr = expr;
        this.loop = Truffle.getRuntime().createLoopNode(new RepeatInfiniteNode());
    }

    final class RepeatInfiniteNode extends Node implements RepeatingNode {
    	@Override
		public boolean executeRepeating(VirtualFrame frame) {
			expr.executeGeneric(frame);
			return true;
		}
    	
    }
    
    @Override
    public Object executeGeneric(final VirtualFrame virtualFrame) {
    	return loop.execute(virtualFrame);
    }
}
