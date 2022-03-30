package com.ihorak.truffle.convertor;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.convertor.context.ParsingContext;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.builtin.CurrentMillisecondsExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.DisplayExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.EvalExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.NewlineExprNode;
import com.ihorak.truffle.node.exprs.builtin.arithmetic.*;
import com.ihorak.truffle.node.exprs.builtin.list.*;
import com.ihorak.truffle.node.exprs.builtin.logical.*;
import com.ihorak.truffle.node.literals.BooleanLiteralNode;
import com.ihorak.truffle.node.literals.LongLiteralNode;

import java.util.ArrayList;
import java.util.List;

public class BuiltinFactory {

    public static SchemeExpression createDivideBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() == 0)
            throw new SchemeException("/: arity mismatch; Expected number of arguments does not match the given number\nexpected: at least 1\ngiven: 0", null);
        if (arguments.size() == 1) return DivideOneArgumentExprNodeGen.create(arguments.get(0));
        return reduceDivide(arguments);
    }

    private static SchemeExpression reduceDivide(List<SchemeExpression> arguments) {
        if (arguments.size() > 2) {
            var right = arguments.remove(arguments.size() - 1);
            return DivideTestNodeGen.create(reduceDivide(arguments), right);
        } else {
            return DivideTestNodeGen.create(arguments.get(0), arguments.get(1));
        }
    }

    public static SchemeExpression createMinusBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() == 0)
            throw new SchemeException("-: arity mismatch; Expected number of arguments does not match the given number\nexpected: at least 1\ngiven: 0", null);
        if (arguments.size() == 1) return NegateNumberExprNodeGen.create(arguments.get(0));
        return reduceMinus(arguments);
    }

    private static SchemeExpression reduceMinus(List<SchemeExpression> arguments) {
        if (arguments.size() > 2) {
            var right = arguments.remove(arguments.size() - 1);
            return MinusTestNodeGen.create(reduceMinus(arguments), right);
        } else {
            return MinusTestNodeGen.create(arguments.get(0), arguments.get(1));
        }
    }

    // (define fun (lambda (x y) (+ x y))
    // +
    // (define plus +) (plus 1 2) plus
    // (map + foo (list 2 3) (list 5 7)) --> (list (+ 2 5) (3 7))
    public static SchemeExpression createPlusBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() == 0) return new LongLiteralNode(0);
        if (arguments.size() == 1) return OneArgumentExprNodeGen.create(arguments.get(0));
        return reducePlus(arguments);
    }

    private static SchemeExpression reducePlus(List<SchemeExpression> arguments) {
        if (arguments.size() > 2) {
            return PlusTestNodeGen.create(arguments.remove(0), reducePlus(arguments));
        } else {
            return PlusTestNodeGen.create(arguments.get(0), arguments.get(1));
        }
    }

    public static SchemeExpression createMultipleBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() == 0) return new LongLiteralNode(1);
        if (arguments.size() == 1) return OneArgumentExprNodeGen.create(arguments.get(0));
        return reduceMultiply(arguments);
    }

    private static SchemeExpression reduceMultiply(List<SchemeExpression> arguments) {
        if (arguments.size() > 2) {
            return MultiplyTestNodeGen.create(arguments.remove(0), reduceMultiply(arguments));
        } else {
            return MultiplyTestNodeGen.create(arguments.get(0), arguments.get(1));
        }
    }

    public static SchemeExpression createEvalBuiltin(List<SchemeExpression> arguments, ParsingContext context) {
        if (arguments.size() == 1) {
            return EvalExprNodeGen.create(arguments.get(0));
        } else {
            throw new SchemeException("eval: arity mismatch; Expected number of arguments does not match the given number \n expected: 1 \n given: " + arguments.size(), null);
        }
    }

    public static SchemeExpression createListBuiltin(List<SchemeExpression> arguments) {
        return new ListExprNode(arguments);
    }

    public static SchemeExpression createConsBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() == 2) {
            return ConsExprNodeGen.create(arguments.get(0), arguments.get(1));
        } else {
            throw new SchemeException("cons: arity mismatch; Expected number of arguments does not match the given number\nexpected: 2\ngiven: " + arguments.size(), null);
        }
    }

    public static SchemeExpression createCdrBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() == 1) {
            return CdrExprNodeGen.create(arguments.get(0));
        } else {
            throw new SchemeException("cdr: arity mismatch; Expected number of arguments does not match the given number \n expected: 1 \n given: " + arguments.size(), null);
        }
    }

    public static SchemeExpression createCarBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() == 1) {
            return CarExprNodeGen.create(arguments.get(0));
        } else {
            throw new SchemeException("car: arity mismatch; Expected number of arguments does not match the given number \n expected: 1 \n given: " + arguments.size(), null);
        }
    }

    public static SchemeExpression createLengthBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() == 1) {
            return LengthExprNodeGen.create(arguments.get(0));
        } else {
            throw new SchemeException("length: arity mismatch; Expected number of arguments does not match the given number \n expected: 1 \n given: " + arguments.size(), null);
        }
    }

    public static SchemeExpression createAppendBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() == 0) return new ListExprNode();
        if (arguments.size() == 1) return AppendExprNodeGen.create(arguments.get(0), new ListExprNode());
        return reduceAppend(arguments);
    }

    private static AppendExprNode reduceAppend(List<SchemeExpression> arguments) {
        if (arguments.size() > 2) {
            return AppendExprNodeGen.create(arguments.remove(0), reduceAppend(arguments));
        } else {
            return AppendExprNodeGen.create(arguments.get(0), arguments.get(1));
        }
    }

    public static SchemeExpression createMapBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() > 1) {
            return new MapExprNode(arguments.remove(0), arguments);
        } else {
            throw new SchemeException("map: arity mismatch; Expected number of argument does not match the given number \n expected: at least 2 \n given: " + (arguments.size()), null);
        }
    }

    public static SchemeExpression createLessThenOrEqual(List<SchemeExpression> arguments) {
        if (arguments.size() == 0)
            throw new SchemeException("<=: arity mismatch; Expected number of argument does not match the given number\nexpected: at least 1\ngiven: 0", null);
        if (arguments.size() == 1) return new BooleanLiteralNode(true);
        if (arguments.size() == 2) return LessThenEqualExprNodeGen.create(arguments.get(0), arguments.get(1));
        return new ReduceComparisonExprNode(reduceLessThenOrEqual(arguments));
    }

    private static List<SchemeExpression> reduceLessThenOrEqual(List<SchemeExpression> arguments) {
        List<SchemeExpression> result = new ArrayList<>();
        for (int i = 0; i < arguments.size() - 1; i++) {
            result.add(LessThenEqualExprNodeGen.create(arguments.get(i), arguments.get(i + 1)));
        }
        return result;
    }

    public static SchemeExpression createEqual(List<SchemeExpression> arguments) {
        if (arguments.size() == 0)
            throw new SchemeException("=: arity mismatch; Expected number of argument does not match the given number\nexpected: at least 1\ngiven: 0", null);
        if (arguments.size() == 1) return new BooleanLiteralNode(true);
        if (arguments.size() == 2) return EqualExprNodeGen.create(arguments.get(0), arguments.get(1));
        return new ReduceComparisonExprNode(reduceEqual(arguments));
    }

    private static List<SchemeExpression> reduceEqual(List<SchemeExpression> arguments) {
        List<SchemeExpression> result = new ArrayList<>();
        for (int i = 0; i < arguments.size() - 1; i++) {
            result.add(EqualExprNodeGen.create(arguments.get(i), arguments.get(i + 1)));
        }
        return result;
    }

    public static SchemeExpression createLessThen(List<SchemeExpression> arguments) {
        if (arguments.size() == 0)
            throw new SchemeException("<: arity mismatch; Expected number of argument does not match the given number\nexpected: at least 1\ngiven: 0", null);
        if (arguments.size() == 1) return new BooleanLiteralNode(true);
        if (arguments.size() == 2) return LessThenExprNodeGen.create(arguments.get(0), arguments.get(1));
        return new ReduceComparisonExprNode(reduceLessThen(arguments));
    }

    public static SchemeExpression createMoreThen(List<SchemeExpression> arguments) {
        if (arguments.size() == 0)
            throw new SchemeException(">: arity mismatch; Expected number of argument does not match the given number\nexpected: at least 1\ngiven: 0", null);
        if (arguments.size() == 1) return new BooleanLiteralNode(true);
        if (arguments.size() == 2) return MoreThenExprNodeGen.create(arguments.get(0), arguments.get(1));
        return new ReduceComparisonExprNode(reduceMoreThen(arguments));
    }

    private static List<SchemeExpression> reduceMoreThen(List<SchemeExpression> arguments) {
        List<SchemeExpression> result = new ArrayList<>();

        for (int i = 0; i < arguments.size() - 1; i++) {
            result.add(MoreThenExprNodeGen.create(arguments.get(i), arguments.get(i + 1)));
        }
        return result;
    }

    public static SchemeExpression createMoreThenEqual(List<SchemeExpression> arguments) {
        if (arguments.size() == 0)
            throw new SchemeException(">=: arity mismatch; Expected number of argument does not match the given number\nexpected: at least 1\ngiven: 0", null);
        if (arguments.size() == 1) return new BooleanLiteralNode(true);
        if (arguments.size() == 2) return MoreThenEqualExprNodeGen.create(arguments.get(0), arguments.get(1));
        return new ReduceComparisonExprNode(reduceMoreThenEqual(arguments));
    }

    private static List<SchemeExpression> reduceMoreThenEqual(List<SchemeExpression> arguments) {
        List<SchemeExpression> result = new ArrayList<>();

        for (int i = 0; i < arguments.size() - 1; i++) {
            result.add(MoreThenEqualExprNodeGen.create(arguments.get(i), arguments.get(i + 1)));
        }
        return result;
    }


    private static List<SchemeExpression> reduceLessThen(List<SchemeExpression> arguments) {
        List<SchemeExpression> result = new ArrayList<>();
        for (int i = 0; i < arguments.size() - 1; i++) {
            result.add(LessThenExprNodeGen.create(arguments.get(i), arguments.get(i + 1)));
        }
        return result;
    }

    public static SchemeExpression createCurrentMillisBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() == 0) {
            return CurrentMillisecondsExprNodeGen.create();
        }
        throw new SchemeException("current-milliseconds: arity mismatch; Expected number of arguments does not match the given number\nExpected: 0\nGiven: " + arguments.size(), null);
    }

    public static SchemeExpression createDisplayBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() == 1) {
            return DisplayExprNodeGen.create(arguments.get(0));
        }
        throw new SchemeException("display: arity mismatch; Expected number of arguments does not match the given number\nExpected: 1\nGiven: " + arguments.size(), null);
    }

    public static SchemeExpression createNewlineBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() == 0) {
            return new NewlineExprNode();
        }
        throw new SchemeException("newline: arity mismatch; Expected number of arguments does not match the given number\nExpected: 0\nGiven: " + arguments.size(), null);
    }
}
