package com.ihorak.truffle.node.exprs.shared;

import com.ihorak.truffle.node.exprs.GivenNumberOfArgsBuiltin;
import com.ihorak.truffle.node.exprs.core.list.CarCoreNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;

//public abstract class CarExpr extends GivenNumberOfArgsBuiltin {
//
//    @Specialization
//    protected Object doCar(Object object, @Cached CarCoreNode carNode) {
//        return carNode.execute(object);
//    }
//}
