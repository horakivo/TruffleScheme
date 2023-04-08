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
import com.ihorak.truffle.node.exprs.shared.*;
import com.ihorak.truffle.node.literals.BooleanLiteralNode;
import com.ihorak.truffle.node.literals.LongLiteralNode;
import com.ihorak.truffle.node.polyglot.ReadForeignGlobalScopeExprNodeGen;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

import javax.xml.transform.Source;
import java.util.ArrayList;
import java.util.List;

import static com.ihorak.truffle.convertor.callable.BuiltinConverter.*;

public class BuiltinFactory {

    public static SchemeExpression createDivideBuiltin(List<SchemeExpression> arguments, @Nullable ParserRuleContext divideCtx) {
        if (arguments.size() == 0) throw ConverterException.arityException("/", 1, 0);
        if (arguments.size() == 1) {
            return SourceSectionUtil.setSourceSectionAndReturnExpr(DivideOneArgumentExprNodeGen.create(arguments.get(0)), divideCtx);
        }
        return SourceSectionUtil.setSourceSectionAndReturnExpr(reduceDivide(arguments), divideCtx);
    }

    private static SchemeExpression reduceDivide(List<SchemeExpression> arguments) {
        if (arguments.size() > 2) {
            var right = arguments.remove(arguments.size() - 1);
            return new DivideExprNode(reduceDivide(arguments), right);
        } else {
            return new DivideExprNode(arguments.get(0), arguments.get(1));
        }
    }

    public static SchemeExpression createMinusBuiltin(List<SchemeExpression> arguments, @Nullable ParserRuleContext minusCtx) {
        if (arguments.size() == 0) throw ConverterException.arityException("-", 1, 0);
        if (arguments.size() == 1)
            return SourceSectionUtil.setSourceSectionAndReturnExpr(NegateNumberExprNodeGen.create(arguments.get(0)), minusCtx);
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

    public static SchemeExpression createPlusBuiltin(List<SchemeExpression> arguments, @Nullable ParserRuleContext plusCtx) {
        if (arguments.size() == 0)
            return SourceSectionUtil.setSourceSectionAndReturnExpr(new LongLiteralNode(0), plusCtx);
        if (arguments.size() == 1)
            return SourceSectionUtil.setSourceSectionAndReturnExpr(OneArgumentExprNodeGen.create(arguments.get(0)), plusCtx);
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

    public static SchemeExpression createMultipleBuiltin(List<SchemeExpression> arguments, @Nullable ParserRuleContext multiplyCtx) {
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

    public static SchemeExpression createListBuiltin(List<SchemeExpression> arguments, @Nullable ParserRuleContext listCtx) {
        var expr = ListExprNodeFactory.create(new ConvertSchemeExprsArgumentsNode(arguments.toArray(SchemeExpression[]::new)));
        return SourceSectionUtil.setSourceSectionAndReturnExpr(expr, listCtx);
    }

    public static SchemeExpression createConsBuiltin(List<SchemeExpression> arguments, @Nullable ParserRuleContext consCtx) {
        int expectedSize = ConsExprNodeFactory.getInstance().getExecutionSignature().size();
        if (arguments.size() == expectedSize) {
            var consExpr = ConsExprNodeFactory.create(arguments.toArray(SchemeExpression[]::new));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(consExpr, consCtx);
        } else {
            throw ConverterException.arityException("cons", expectedSize, arguments.size());
        }
    }

    public static SchemeExpression createCdrBuiltin(List<SchemeExpression> arguments, @Nullable ParserRuleContext cdrCtx) {
        int expectedSize = CdrExprNodeFactory.getInstance().getExecutionSignature().size();
        if (arguments.size() == expectedSize) {
            var cdrExpr = CdrExprNodeFactory.create(arguments.toArray(SchemeExpression[]::new));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(cdrExpr, cdrCtx);
        } else {
            throw ConverterException.arityException("cdr", 1, arguments.size());
        }
    }

    public static SchemeExpression createCarBuiltin(List<SchemeExpression> arguments, @Nullable ParserRuleContext carCtx) {
        int expectedSize = CarExprNodeFactory.getInstance().getExecutionSignature().size();
        if (arguments.size() == expectedSize) {
            var carExpr = CarExprNodeFactory.create(arguments.toArray(SchemeExpression[]::new));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(carExpr, carCtx);
        } else {
            throw ConverterException.arityException("car", expectedSize, arguments.size());
        }
    }

    public static SchemeExpression createLengthBuiltin(List<SchemeExpression> arguments, @Nullable ParserRuleContext lengthCtx) {
        if (arguments.size() == 1) {
            var expr = LengthExprNodeFactory.create(arguments.toArray(SchemeExpression[]::new));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(expr, lengthCtx);
        } else {
            throw ConverterException.arityException("length", 1, arguments.size());
        }
    }

    //    //TODO this is messy, consider using binary reducibility instead of current approach. Study what is better
    public static SchemeExpression createAppendBuiltin(List<SchemeExpression> arguments, @Nullable ParserRuleContext appendCtx) {
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

    public static SchemeExpression createLessThenOrEqual(List<SchemeExpression> arguments, @Nullable ParserRuleContext ctx) {
        if (arguments.size() == 0) throw ConverterException.arityException("<=", 1, 0);
        if (arguments.size() == 1)
            return SourceSectionUtil.setSourceSectionAndReturnExpr(new BooleanLiteralNode(true), ctx);
        if (arguments.size() == 2)
            return SourceSectionUtil.setSourceSectionAndReturnExpr(new LessThenEqualExprNode(arguments.get(0), arguments.get(1)), ctx);
        var expr = new ReduceComparisonExprNode(reduceLessThenOrEqual(arguments));
        return SourceSectionUtil.setSourceSectionAndReturnExpr(expr, ctx);
    }

    private static List<SchemeExpression> reduceLessThenOrEqual(List<SchemeExpression> arguments) {
        List<SchemeExpression> result = new ArrayList<>();
        for (int i = 0; i < arguments.size() - 1; i++) {
            result.add(new LessThenEqualExprNode(arguments.get(i), arguments.get(i + 1)));
        }
        return result;
    }

    public static SchemeExpression createEqualNumbers(List<SchemeExpression> arguments, @Nullable ParserRuleContext equalCtx) {
        if (arguments.size() == 0) throw ConverterException.arityException("=", 1, 0);
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

    public static SchemeExpression createLessThen(List<SchemeExpression> arguments, @Nullable ParserRuleContext ctx) {
        if (arguments.size() == 0) throw ConverterException.arityException("<", 1, 0);
        if (arguments.size() == 1)
            return SourceSectionUtil.setSourceSectionAndReturnExpr(new BooleanLiteralNode(true), ctx);
        if (arguments.size() == 2) {
            var lessExpr = new LessThenExprNode(arguments.get(0), arguments.get(1));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(lessExpr, ctx);
        }
        var expr = new ReduceComparisonExprNode(reduceLessThen(arguments));
        return SourceSectionUtil.setSourceSectionAndReturnExpr(expr, ctx);
    }

    public static SchemeExpression createMoreThen(List<SchemeExpression> arguments, @Nullable ParserRuleContext ctx) {
        if (arguments.size() == 0) throw ConverterException.arityException(">", 1, 0);
        if (arguments.size() == 1)
            return SourceSectionUtil.setSourceSectionAndReturnExpr(new BooleanLiteralNode(true), ctx);
        if (arguments.size() == 2)
            return SourceSectionUtil.setSourceSectionAndReturnExpr(new MoreThenExprNode(arguments.get(0), arguments.get(1)), ctx);
        var expr = new ReduceComparisonExprNode(reduceMoreThen(arguments));
        return SourceSectionUtil.setSourceSectionAndReturnExpr(expr, ctx);
    }

    private static List<SchemeExpression> reduceMoreThen(List<SchemeExpression> arguments) {
        List<SchemeExpression> result = new ArrayList<>();

        for (int i = 0; i < arguments.size() - 1; i++) {
            result.add(new MoreThenExprNode(arguments.get(i), arguments.get(i + 1)));
        }
        return result;
    }

    public static SchemeExpression createMoreThenEqual(List<SchemeExpression> arguments, @Nullable ParserRuleContext ctx) {
        if (arguments.size() == 0) throw ConverterException.arityException(">=", 1, 0);
        if (arguments.size() == 1)
            return SourceSectionUtil.setSourceSectionAndReturnExpr(new BooleanLiteralNode(true), ctx);
        if (arguments.size() == 2)
            return SourceSectionUtil.setSourceSectionAndReturnExpr(new MoreThenEqualExprNode(arguments.get(0), arguments.get(1)), ctx);
        var expr = new ReduceComparisonExprNode(reduceMoreThenEqual(arguments));
        return SourceSectionUtil.setSourceSectionAndReturnExpr(expr, ctx);
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

    public static SchemeExpression createCurrentMillisBuiltin(List<SchemeExpression> arguments, @Nullable ParserRuleContext ctx) {
        if (arguments.size() == 0) {
            return SourceSectionUtil.setSourceSectionAndReturnExpr(CurrentMillisecondsExprNodeGen.create(), ctx);
        }
        throw ConverterException.arityException("current-milliseconds", 0, arguments.size());
    }

    public static SchemeExpression createDisplayBuiltin(List<SchemeExpression> arguments, @Nullable ParserRuleContext ctx) {
        if (arguments.size() == 1) {
            return SourceSectionUtil.setSourceSectionAndReturnExpr(DisplayExprNodeGen.create(arguments.get(0)), ctx);
        }
        throw ConverterException.arityException("display", 1, arguments.size());
    }

    public static SchemeExpression createNot(List<SchemeExpression> arguments, @Nullable ParserRuleContext ctx) {
        if (arguments.size() == 1) {
            var expr = new NotExprNode(BooleanCastExprNodeGen.create(arguments.get(0)));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(expr, ctx);
        }
        throw ConverterException.arityException("not", 1, arguments.size());
    }

    public static SchemeExpression createIsNull(List<SchemeExpression> arguments, @Nullable ParserRuleContext nullCtx) {
        if (arguments.size() == 1) {
            return SourceSectionUtil.setSourceSectionAndReturnExpr(IsNullExprNodeGen.create(arguments.get(0)), nullCtx);
        }
        throw ConverterException.arityException("null", 1, arguments.size());
    }

    public static SchemeExpression createModulo(List<SchemeExpression> arguments, @Nullable ParserRuleContext moduleCtx) {
        if (arguments.size() == 2) {
            var moduloExpr = ModuloExprNodeGen.create(arguments.get(0), arguments.get(1));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(moduloExpr, moduleCtx);
        }
        throw ConverterException.arityException("modulo", 2, arguments.size());
    }

    public static SchemeExpression createCadr(List<SchemeExpression> arguments, @Nullable ParserRuleContext cadrCtx) {
        if (arguments.size() == 1) {
            var cdrExpr = CdrExprNodeFactory.create(arguments.toArray(SchemeExpression[]::new));
            var carExpr = CarExprNodeFactory.create(new SchemeExpression[]{cdrExpr});

            return SourceSectionUtil.setSourceSectionAndReturnExpr(carExpr, cadrCtx);
        }
        throw ConverterException.arityException("cadr", 1, arguments.size());
    }

    public static SchemeExpression createEqual(List<SchemeExpression> arguments, @Nullable ParserRuleContext equalCtx) {
        if (arguments.size() == 2) {
            var equalExpr = new EqualExprNode(arguments.get(0), arguments.get(1));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(equalExpr, equalCtx);
        }
        throw ConverterException.arityException("equal", 2, arguments.size());
    }

    public static SchemeExpression createEvalSource(List<SchemeExpression> arguments, @Nullable ParserRuleContext evalSourceCtx) {
        if (arguments.size() == 2) {
            var evalSourceExpr = EvalSourceExprNodeGen.create(arguments.get(0), arguments.get(1));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(evalSourceExpr, evalSourceCtx);
        }
        throw ConverterException.arityException(POLYGLOT_EVAL_SOURCE, 2, arguments.size());
    }

    public static SchemeExpression createReadForeignGlobalScope(List<SchemeExpression> arguments, @Nullable ParserRuleContext evalSourceCtx) {
        if (arguments.size() == 2) {
            var polyglotProc = ReadForeignGlobalScopeExprNodeGen.create(arguments.get(0), arguments.get(1));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(polyglotProc, evalSourceCtx);
        }
        throw ConverterException.arityException(POLYGLOT_READ_GLOBAL_SCOPE, 2, arguments.size());
    }

}
