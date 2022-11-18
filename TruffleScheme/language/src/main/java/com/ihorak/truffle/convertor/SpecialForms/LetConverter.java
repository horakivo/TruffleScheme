package com.ihorak.truffle.convertor.SpecialForms;

import com.ihorak.truffle.convertor.ListToExpressionConverter;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.util.CreateWriteExprNode;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNode;
import com.ihorak.truffle.node.special_form.LetExprNode;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeSymbol;

import java.util.ArrayList;
import java.util.List;

public class LetConverter {

    private LetConverter() {}

    public static LetExprNode convert(SchemeCell letList, ParsingContext context) {
        validate(letList);
        ParsingContext letContext = new ParsingContext(context);

        SchemeCell localBindings = (SchemeCell) letList.get(1);
        SchemeCell body = letList.cdr.cdr;

        List<WriteLocalVariableExprNode> bindingExpressions = createWriteLocalVariables(localBindings, letContext);
        List<SchemeExpression> bodyExpressions = createBodyExpr(body, letContext);

        List<SchemeExpression> bindingsAndBodyExpressions = new ArrayList<>(bindingExpressions.size() + bodyExpressions.size());
        bindingsAndBodyExpressions.addAll(bindingExpressions);
        bindingsAndBodyExpressions.addAll(bodyExpressions);

        return new LetExprNode(bindingsAndBodyExpressions);

    }

    private static List<WriteLocalVariableExprNode> createWriteLocalVariables(SchemeCell localBindings, ParsingContext context) {
        List<WriteLocalVariableExprNode> result = new ArrayList<>();
        List<SchemeExpression> expressions = new ArrayList<>();
        List<SchemeSymbol> symbols = new ArrayList<>();
        for (Object obj : localBindings) {
            var bindingList = (SchemeCell) obj;
            var name = (SchemeSymbol) bindingList.get(0);
            var dataExpr = bindingList.get(1);
            var expr = ListToExpressionConverter.convert(dataExpr, context);
            expressions.add(expr);
            symbols.add(name);
        }

        for (int i = 0; i < symbols.size(); i++) {
            result.add(CreateWriteExprNode.createWriteLocalVariableExprNode(symbols.get(i), expressions.get(i), context));
        }

        return result;
    }

    private static List<SchemeExpression> createBodyExpr(SchemeCell body, ParsingContext letContext) {
        List<SchemeExpression> result = new ArrayList<>();
        for (Object obj : body) {
            result.add(ListToExpressionConverter.convert(obj, letContext));
        }

        return result;
    }

    private static void validate(SchemeCell letList) {
        var localBinding = letList.get(1);
        if (!(localBinding instanceof SchemeCell localBindingList)) {
            throw new SchemeException("let: contract violation\nexpected: (let ((<symbol1>> <expr>>) .. (<symbol2> <expr>) ...) <>)", null);
        }

        List<SchemeSymbol> allIdentifiers = new ArrayList<>();
        for (Object obj : localBindingList) {
            if (!(obj instanceof SchemeCell list)) {
                throw new SchemeException("let: contract violation\nexpected: (let ((id1 val-expr1) (id2 val-expr2) ...) body ...)", null);
            }

            if (list.size() != 2) {
                throw new SchemeException("let: bad syntax\nexpected size of binding is 2\ngiven: " + list.size(), null);
            }

            if (!(list.car instanceof SchemeSymbol symbol)) {
                throw new SchemeException("let: contract violation\nexpected: identifier\ngiven: " + list.car, null);
            }

            if (allIdentifiers.contains(symbol)) {
                throw new SchemeException("let: duplicate identifier " + symbol, null);
            } else {
                allIdentifiers.add(symbol);
            }
        }

    }
}
