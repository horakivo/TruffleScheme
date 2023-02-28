package com.ihorak.truffle.convertor.PrimitiveTypes;

import com.ihorak.truffle.convertor.SourceSectionUtil;
import com.ihorak.truffle.node.literals.LongLiteralNode;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;


public class LongConverter {

    private LongConverter() {
    }

    public static LongLiteralNode convert(long value, ParserRuleContext ctx) {
        var node =  new LongLiteralNode(value);
        SourceSectionUtil.setSourceSection(node, ctx);

        return node;
    }

}
