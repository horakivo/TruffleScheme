package com.ihorak.truffle.convertor.SpecialForms;

import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNode;
import com.ihorak.truffle.node.special_form.LetExprNode;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeSymbol;

import java.util.ArrayList;
import java.util.List;

public class LetrecConverter extends AbstractLetConverter{

    private LetrecConverter() {}

//    public static LetExprNode convert(SchemeCell letList, ParsingContext context) {
//        validate(letList);
//        ParsingContext letContext = new ParsingContext(context);
//
//        SchemeCell localBindings = (SchemeCell) letList.get(1);
//        SchemeCell body = letList.cdr.cdr;
//
//        List<WriteLocalVariableExprNode> bindingExpressions = createWriteLocalVariables(localBindings, letContext);
//        List<SchemeExpression> bodyExpressions = createBodyExpr(body, letContext);
//
//        List<SchemeExpression> bindingsAndBodyExpressions = new ArrayList<>(bindingExpressions.size() + bodyExpressions.size());
//        bindingsAndBodyExpressions.addAll(bindingExpressions);
//        bindingsAndBodyExpressions.addAll(bodyExpressions);
//
//        return new LetExprNode(bindingsAndBodyExpressions);
//
//    }
//
//    private static List<WriteLocalVariableExprNode> createWriteLocalVariables(SchemeCell localBindings, ParsingContext letContext) {
//        List<SchemeCell> dataExpressions = new ArrayList<>();
//        List<SchemeSymbol> symbols = new ArrayList<>();
//        for (Object obj : localBindings) {
//            var bindingList = (SchemeCell) obj;
//            symbols.add((SchemeSymbol) bindingList.get(0));
//            dataExpressions.add((SchemeCell) bindingList.get(1));
//        }
//
//        for (SchemeSymbol symbol : symbols) {
//            letContext.addLocalSymbol(symbol)
//        }
//    }

}
