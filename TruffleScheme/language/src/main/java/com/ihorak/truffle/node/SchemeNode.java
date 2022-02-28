package com.ihorak.truffle.node;

import com.ihorak.truffle.type.SchemeTypes;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.nodes.Node;

@TypeSystemReference(SchemeTypes.class)
public abstract class SchemeNode extends Node {

}
