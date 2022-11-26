package com.ihorak.truffle.node.callable.TCO;


import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.frame.VirtualFrame;

/** Put in place of every non-tail call */
public class TailCallCatcherNode extends SchemeExpression {



    @Override
    public Object executeGeneric(final VirtualFrame virtualFrame) {
        return null;
    }
}
