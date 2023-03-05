package com.ihorak.truffle.convertor;


import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.cast.BooleanCastExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.*;
import com.ihorak.truffle.node.exprs.builtin.arithmetic.*;
import com.ihorak.truffle.node.exprs.builtin.comparison.*;
import com.ihorak.truffle.node.exprs.builtin.list.AppendExprNode1;
import com.ihorak.truffle.node.exprs.builtin.list.AppendExprNode1NodeGen;
import com.ihorak.truffle.node.exprs.builtin.list.ListRefExprNodeGen;
import com.ihorak.truffle.node.exprs.shared.*;
import com.ihorak.truffle.node.literals.BooleanLiteralNode;
import com.ihorak.truffle.node.literals.LongLiteralNode;
import org.antlr.v4.runtime.ParserRuleContext;

import javax.xml.transform.Source;
import java.util.ArrayList;
import java.util.List;

public class BuiltinFactory {

    public static SchemeExpression createDivideBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() == 0)
            throw new SchemeException(
                    "/: arity mismatch; Expected number of arguments does not match the given number\nexpected: at least 1\ngiven: 0", null);
        if (arguments.size() == 1) return DivideOneArgumentExprNodeGen.create(arguments.get(0));
        return reduceDivide(arguments);
    }

    private static SchemeExpression reduceDivide(List<SchemeExpression> arguments) {
        if (arguments.size() > 2) {
            var right = arguments.remove(arguments.size() - 1);
            return new DivideExprNode(reduceDivide(arguments), right);
        } else {
            return new DivideExprNode(arguments.get(0), arguments.get(1));
        }
    }

    public static SchemeExpression createMinusBuiltin(List<SchemeExpression> arguments, ParserRuleContext minusCtx) {
        if (arguments.size() == 0)
            throw new SchemeException(
                    "-: arity mismatch; Expected number of arguments does not match the given number\nexpected: at least 1\ngiven: 0", null);
        if (arguments.size() == 1) return NegateNumberExprNodeGen.create(arguments.get(0));
        var minusExpr = reduceMinus(arguments);
        return SourceSectionUtil.setSourceSectionAndReturnExpr(minusExpr, minusCtx);
    }

    private static SchemeExpression reduceMinus(List<SchemeExpression> arguments) {
        if (arguments.size() > 2) {
            var right = arguments.remove(arguments.size() - 1);
            return new MinusExprNode(reduceMinus(arguments), right);
        } else {
            return new MinusExprNode(arguments.get(0), arguments.get(1));
        }
    }

    public static SchemeExpression createPlusBuiltin(List<SchemeExpression> arguments, ParserRuleContext plusCtx) {
        if (arguments.size() == 0) return new LongLiteralNode(0);
        if (arguments.size() == 1) return OneArgumentExprNodeGen.create(arguments.get(0));
        var plusExpr = reducePlus(arguments);
        return SourceSectionUtil.setSourceSectionAndReturnExpr(plusExpr, plusCtx);
    }

    private static SchemeExpression reducePlus(List<SchemeExpression> arguments) {
        if (arguments.size() > 2) {
            return new PlusExprNode(arguments.remove(0), reducePlus(arguments));
        } else {
            return new PlusExprNode(arguments.get(0), arguments.get(1));
        }
    }

    public static SchemeExpression createMultipleBuiltin(List<SchemeExpression> arguments, ParserRuleContext multiplyCtx) {
        if (arguments.size() == 0) {
            return SourceSectionUtil.setSourceSectionAndReturnExpr(new LongLiteralNode(1), multiplyCtx);
        }
        if (arguments.size() == 1) {
            return SourceSectionUtil.setSourceSectionAndReturnExpr(OneArgumentExprNodeGen.create(arguments.get(0)), multiplyCtx);
        }
        return SourceSectionUtil.setSourceSectionAndReturnExpr(reduceMultiply(arguments), multiplyCtx);
    }

    private static SchemeExpression reduceMultiply(List<SchemeExpression> arguments) {
        if (arguments.size() > 2) {
            return new MultiplyExprNode(arguments.remove(0), reduceMultiply(arguments));
        } else {
            return new MultiplyExprNode(arguments.get(0), arguments.get(1));
        }
    }

    public static SchemeExpression createEvalBuiltin(List<SchemeExpression> arguments, ParsingContext context) {
//        int expectedSize = EvalExprNodeFactory.getInstance().getExecutionSignature().size();
//        if (arguments.size() == expectedSize) {
//            return EvalExprNodeFactory.create(arguments.toArray(SchemeExpression[]::new));
//        } else {
//            throw new SchemeException(
//                    "eval: arity mismatch; Expected number of arguments does not match the given number\nexpected: " + expectedSize + "\ngiven: " + arguments.size(),
//                    null);
//        }
        return null;
    }

    public static SchemeExpression createListBuiltin(List<SchemeExpression> arguments) {
        return ListExprNodeFactory.create(new ConvertSchemeExprsArgumentsNode(arguments.toArray(SchemeExpression[]::new)));
    }

    public static SchemeExpression createConsBuiltin(List<SchemeExpression> arguments, ParserRuleContext consCtx) {
        int expectedSize = ConsExprNodeFactory.getInstance().getExecutionSignature().size();
        if (arguments.size() == expectedSize) {
            var consExpr = ConsExprNodeFactory.create(arguments.toArray(SchemeExpression[]::new));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(consExpr, consCtx);
        } else {
            throw new SchemeException(
                    "cons: arity mismatch; Expected number of arguments does not match the given number\nexpected: " + expectedSize + "\ngiven: " + arguments.size(),
                    null);
        }
    }

    public static SchemeExpression createCdrBuiltin(List<SchemeExpression> arguments, ParserRuleContext cdrCtx) {
        int expectedSize = CdrExprNodeFactory.getInstance().getExecutionSignature().size();
        if (arguments.size() == expectedSize) {
            var cdrExpr = CdrExprNodeFactory.create(arguments.toArray(SchemeExpression[]::new));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(cdrExpr, cdrCtx);
        } else {
            throw new SchemeException(
                    "cdr: arity mismatch; Expected number of arguments does not match the given number \n expected: 1 \n given: " + arguments.size(),
                    null);
        }
    }

    public static SchemeExpression createCarBuiltin(List<SchemeExpression> arguments, ParserRuleContext carCtx) {
        int expectedSize = CarExprNodeFactory.getInstance().getExecutionSignature().size();
        if (arguments.size() == expectedSize) {
            var carExpr = CarExprNodeFactory.create(arguments.toArray(SchemeExpression[]::new));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(carExpr, carCtx);
        } else {
            throw new SchemeException(
                    "car: arity mismatch; Expected number of arguments does not match the given number\nexpected: " + expectedSize + "\ngiven: " + arguments.size(),
                    null);
        }
    }

    public static SchemeExpression createLengthBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() == 1) {
            return LengthExprNodeFactory.create(arguments.toArray(SchemeExpression[]::new));
        } else {
            throw new SchemeException(
                    "length: arity mismatch; Expected number of arguments does not match the given number \n expected: 1 \n given: " + arguments.size(),
                    null);
        }
    }

    //    //TODO this is messy, consider using binary reducibility instead of current approach. Study what is better
    public static SchemeExpression createAppendBuiltin(List<SchemeExpression> arguments, ParserRuleContext appendCtx) {
        if (arguments.isEmpty()) {
            var emptyListExpr = ListExprNodeFactory.create(new ConvertSchemeExprsArgumentsNode(new SchemeExpression[]{}));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(emptyListExpr, appendCtx);
        }
        if (arguments.size() == 1) {
            var oneElementListExpr = AppendExprNode1NodeGen.create(arguments.get(0), ListExprNodeFactory.create(new ConvertSchemeExprsArgumentsNode(new SchemeExpression[]{})));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(oneElementListExpr, appendCtx);
        }
        return SourceSectionUtil.setSourceSectionAndReturnExpr(reduceAppend(arguments), appendCtx);
    }

    private static AppendExprNode1 reduceAppend(List<SchemeExpression> arguments) {
        if (arguments.size() > 2) {
            return AppendExprNode1NodeGen.create(arguments.remove(0), reduceAppend(arguments));
        } else {
            return AppendExprNode1NodeGen.create(arguments.get(0), arguments.get(1));
        }
    }

//    public static SchemeExpression createAppendBuiltin(List<SchemeExpression> arguments) {
//        return null;
//    }

    public static SchemeExpression createMapBuiltin(List<SchemeExpression> arguments) {
        throw new SchemeException("NOT IMPLEMENTED YET", null);
//        if (arguments.size() > 1) {
//            return MapExprNodeFactory.create(new ConvertSchemeExprsArgumentsNode(arguments.toArray(SchemeExpression[]::new)));
//        } else {
//            throw new SchemeException(
//                    "map: arity mismatch; Expected number of argument does not match the given number \n expected: at least 2 \n given: " + (arguments.size()),
//                    null);
//        }
    }

    public static SchemeExpression createLessThenOrEqual(List<SchemeExpression> arguments) {
        if (arguments.size() == 0)
            throw new SchemeException(
                    "<=: arity mismatch; Expected number of argument does not match the given number\nexpected: at least 1\ngiven: 0", null);
        if (arguments.size() == 1) return new BooleanLiteralNode(true);
        if (arguments.size() == 2) return new LessThenEqualExprNode(arguments.get(0), arguments.get(1));
        return new ReduceComparisonExprNode(reduceLessThenOrEqual(arguments));
    }

    private static List<SchemeExpression> reduceLessThenOrEqual(List<SchemeExpression> arguments) {
        List<SchemeExpression> result = new ArrayList<>();
        for (int i = 0; i < arguments.size() - 1; i++) {
            result.add(new LessThenEqualExprNode(arguments.get(i), arguments.get(i + 1)));
        }
        return result;
    }

    public static SchemeExpression createEqualNumbers(List<SchemeExpression> arguments, ParserRuleContext equalCtx) {
        if (arguments.size() == 0)
            throw new SchemeException(
                    "=: arity mismatch; Expected number of argument does not match the given number\nexpected: at least 1\ngiven: 0", null);
        if (arguments.size() == 1) {
            var booleanExpr = new BooleanLiteralNode(true);
            return SourceSectionUtil.setSourceSectionAndReturnExpr(booleanExpr, equalCtx);
        }
        if (arguments.size() == 2) {
            var equalExpr = new EqualNumbersExprNode(arguments.get(0), arguments.get(1));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(equalExpr, equalCtx);
        }

        var reducedEqualExpr = new ReduceComparisonExprNode(reduceEqual(arguments));
        return SourceSectionUtil.setSourceSectionAndReturnExpr(reducedEqualExpr, equalCtx);
    }

    private static List<SchemeExpression> reduceEqual(List<SchemeExpression> arguments) {
        List<SchemeExpression> result = new ArrayList<>();
        for (int i = 0; i < arguments.size() - 1; i++) {
            result.add(new EqualNumbersExprNode(arguments.get(i), arguments.get(i + 1)));
        }
        return result;
    }

    public static SchemeExpression createLessThen(List<SchemeExpression> arguments, ParserRuleContext ctx) {
        if (arguments.size() == 0)
            throw new SchemeException(
                    "<: arity mismatch; Expected number of argument does not match the given number\nexpected: at least 1\ngiven: 0", null);
        if (arguments.size() == 1) return new BooleanLiteralNode(true);
        if (arguments.size() == 2) {
            var lessExpr = new LessThenExprNode(arguments.get(0), arguments.get(1));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(lessExpr, ctx);
        }
        return new ReduceComparisonExprNode(reduceLessThen(arguments));
    }

    public static SchemeExpression createMoreThen(List<SchemeExpression> arguments) {
        if (arguments.size() == 0)
            throw new SchemeException(
                    ">: arity mismatch; Expected number of argument does not match the given number\nexpected: at least 1\ngiven: 0", null);
        if (arguments.size() == 1) return new BooleanLiteralNode(true);
        if (arguments.size() == 2) return new MoreThenExprNode(arguments.get(0), arguments.get(1));
        return new ReduceComparisonExprNode(reduceMoreThen(arguments));
    }

    private static List<SchemeExpression> reduceMoreThen(List<SchemeExpression> arguments) {
        List<SchemeExpression> result = new ArrayList<>();

        for (int i = 0; i < arguments.size() - 1; i++) {
            result.add(new MoreThenExprNode(arguments.get(i), arguments.get(i + 1)));
        }
        return result;
    }

    public static SchemeExpression createMoreThenEqual(List<SchemeExpression> arguments) {
        if (arguments.size() == 0)
            throw new SchemeException(
                    ">=: arity mismatch; Expected number of argument does not match the given number\nexpected: at least 1\ngiven: 0", null);
        if (arguments.size() == 1) return new BooleanLiteralNode(true);
        if (arguments.size() == 2) return new MoreThenEqualExprNode(arguments.get(0), arguments.get(1));
        return new ReduceComparisonExprNode(reduceMoreThenEqual(arguments));
    }

    private static List<SchemeExpression> reduceMoreThenEqual(List<SchemeExpression> arguments) {
        List<SchemeExpression> result = new ArrayList<>();

        for (int i = 0; i < arguments.size() - 1; i++) {
            result.add(new MoreThenEqualExprNode(arguments.get(i), arguments.get(i + 1)));
        }
        return result;
    }

    private static List<SchemeExpression> reduceLessThen(List<SchemeExpression> arguments) {
        List<SchemeExpression> result = new ArrayList<>();
        for (int i = 0; i < arguments.size() - 1; i++) {
            result.add(new LessThenExprNode(arguments.get(i), arguments.get(i + 1)));
        }
        return result;
    }

    public static SchemeExpression createCurrentMillisBuiltin(List<SchemeExpression> arguments, ParserRuleContext ctx) {
        if (arguments.size() == 0) {
            return SourceSectionUtil.setSourceSectionAndReturnExpr(CurrentMillisecondsExprNodeGen.create(), ctx);
        }
        throw new SchemeException(
                "current-milliseconds: arity mismatch; Expected number of arguments does not match the given number\nExpected: 0\nGiven: " + arguments.size(),
                null);
    }

    public static SchemeExpression createDisplayBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() == 1) {
            return DisplayExprNodeGen.create(arguments.get(0));
        }
        throw new SchemeException(
                "display: arity mismatch; Expected number of arguments does not match the given number\nExpected: 1\nGiven: " + arguments.size(),
                null);
    }

    public static SchemeExpression createNewlineBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() == 0) {
            return new NewlineExprNode();
        }
        throw new SchemeException(
                "newline: arity mismatch; Expected number of arguments does not match the given number\nExpected: 0\nGiven: " + arguments.size(),
                null);
    }

    public static SchemeExpression createLoop(List<SchemeExpression> arguments) {
        var number = (LongLiteralNode) arguments.get(0);
        return new LoopExprNode(number.getValue(), arguments.get(1));
    }

    public static SchemeExpression createBegin(List<SchemeExpression> arguments) {
        return new BeginExprNode(arguments);
    }

    public static SchemeExpression createListRef(List<SchemeExpression> arguments) {
        if (arguments.size() == 2) {
            return ListRefExprNodeGen.create(arguments.get(0), arguments.get(1));
        }
        throw new SchemeException(
                "list-ref: arity mismatch; Expected number of arguments does not match the given number\nExpected: 2\nGiven: " + arguments.size(),
                null);
    }

    public static SchemeExpression createNot(List<SchemeExpression> arguments) {
        if (arguments.size() == 1) {
            return new NotExprNode(BooleanCastExprNodeGen.create(arguments.get(0)));
        }

        throw new SchemeException(
                "not: arity mismatch; Expected number of arguments does not match the given number\nExpected: 1\nGiven: " + arguments.size(), null);
    }

    public static SchemeExpression createIsNull(List<SchemeExpression> arguments, ParserRuleContext nullCtx) {
        if (arguments.size() == 1) {
            return SourceSectionUtil.setSourceSectionAndReturnExpr(IsNullExprNodeGen.create(arguments.get(0)), nullCtx);
        }

        throw new SchemeException(
                "null: arity mismatch; Expected number of arguments does not match the given number\nExpected: 1\nGiven: " + arguments.size(), null);
    }

    public static SchemeExpression createModulo(List<SchemeExpression> arguments, ParserRuleContext moduleCtx) {
        if (arguments.size() == 2) {
            var moduloExpr = ModuloExprNodeGen.create(arguments.get(0), arguments.get(1));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(moduloExpr, moduleCtx);
        }

        throw new SchemeException(
                "modulo: arity mismatch; Expected number of arguments does not match the given number\nExpected: 2\nGiven: " + arguments.size(),
                null);
    }

    public static SchemeExpression createCadr(List<SchemeExpression> arguments, ParserRuleContext cadrCtx) {
        if (arguments.size() == 1) {
            var cdrExpr = CdrExprNodeFactory.create(arguments.toArray(SchemeExpression[]::new));
            var carExpr = CarExprNodeFactory.create(new SchemeExpression[]{cdrExpr});

            return SourceSectionUtil.setSourceSectionAndReturnExpr(carExpr, cadrCtx);
        }

        throw new SchemeException(
                "cadr: arity mismatch; Expected number of arguments does not match the given number\nExpected: 1\nGiven: " + arguments.size(),
                null);
    }

    public static SchemeExpression createInfinite(List<SchemeExpression> arguments) {
        return new WhileInfiniteExprNode(arguments.get(0));
    }

    public static SchemeExpression createEqual(List<SchemeExpression> arguments, ParserRuleContext equalCtx) {
        if (arguments.size() == 2) {
            var equalExpr = new EqualExprNode(arguments.get(0), arguments.get(1));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(equalExpr, equalCtx);
        }

        throw new SchemeException(
                "equal: arity mismatch; Expected number of arguments does not match the given number\nExpected: 2\nGiven: " + arguments.size(),
                null);
    }

}
