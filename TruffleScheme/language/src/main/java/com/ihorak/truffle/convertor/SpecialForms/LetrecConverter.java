package com.ihorak.truffle.convertor.SpecialForms;

import com.ihorak.truffle.convertor.ListToExpressionConverter;
import com.ihorak.truffle.convertor.context.LexicalScope;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.convertor.util.CreateWriteExprNode;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNode;
import com.ihorak.truffle.node.special_form.LetExprNode;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeSymbol;

import java.util.ArrayList;
import java.util.List;

public class LetrecConverter extends AbstractLetConverter {

    private LetrecConverter() {}

    //TODO solve code duplication
    public static LetExprNode convert(SchemeCell letList, ParsingContext context) {
        validate(letList);
        ParsingContext letContext = new ParsingContext(context, LexicalScope.LETREC, context.getFrameDescriptorBuilder());

        SchemeCell localBindings = (SchemeCell) letList.get(1);
        SchemeCell body = letList.cdr.cdr;

        List<WriteLocalVariableExprNode> bindingExpressions = createWriteLocalVariables(localBindings, letContext);
        List<SchemeExpression> bodyExpressions = createBodyExpr(body, letContext);

        List<SchemeExpression> bindingsAndBodyExpressions = new ArrayList<>(bindingExpressions.size() + bodyExpressions.size());
        bindingsAndBodyExpressions.addAll(bindingExpressions);
        bindingsAndBodyExpressions.addAll(bodyExpressions);

        return new LetExprNode(bindingsAndBodyExpressions);
    }

    private static List<WriteLocalVariableExprNode> createWriteLocalVariables(SchemeCell localBindings, ParsingContext letContext) {
        List<WriteLocalVariableExprNode> result = new ArrayList<>();
        List<SchemeSymbol> symbols = new ArrayList<>();
        List<Object> dataExpressions = new ArrayList<>();
        for (Object obj : localBindings) {
            var bindingList = (SchemeCell) obj;
            var name = (SchemeSymbol) bindingList.get(0);
            letContext.findOrAddLocalSymbol(name);
            symbols.add(name);
            dataExpressions.add(bindingList.get(1));
        }

        letContext.makeLocalVariablesNullable(symbols);

        for (int i = 0; i < symbols.size(); i++) {
            var expression = ListToExpressionConverter.convert(dataExpressions.get(i), letContext);
            result.add(CreateWriteExprNode.createWriteLocalVariableExprNode(symbols.get(i), expression, letContext));
        }

        letContext.makeLocalVariablesNonNullable(symbols);

        return result;
    }

}
