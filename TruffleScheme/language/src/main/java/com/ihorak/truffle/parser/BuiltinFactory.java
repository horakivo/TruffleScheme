package com.ihorak.truffle.parser;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.context.Context;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.ReduceExprNode;
import com.ihorak.truffle.node.exprs.builtin.CurrentMillisecondsExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.DisplayExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.EvalExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.NewlineExprNode;
import com.ihorak.truffle.node.exprs.builtin.arithmetic.*;
import com.ihorak.truffle.node.exprs.builtin.list.*;
import com.ihorak.truffle.node.exprs.builtin.logical.EqualExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.logical.LessThenEqualExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.logical.LessThenExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.logical.ReduceComparisonExprNode;
import com.ihorak.truffle.node.literals.BooleanLiteralNode;
import com.ihorak.truffle.node.literals.LongLiteralNode;
import com.ihorak.truffle.type.SchemeCell;

import java.util.ArrayList;
import java.util.List;

public class BuiltinFactory {

    public static SchemeExpression createDivideBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() == 0) throw new SchemeException("/: arity mismatch; Expected number of arguments does not match the given number\nexpected: at least 1\ngiven: 0");
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
        if (arguments.size() == 0) throw new SchemeException("-: arity mismatch; Expected number of arguments does not match the given number\nexpected: at least 1\ngiven: 0");
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

    public static SchemeExpression createEvalBuiltin(List<SchemeExpression> arguments, Context context) {
        if (arguments.size() == 1) {
            return EvalExprNodeGen.create(arguments.get(0));
        } else {
            throw new SchemeException("eval: arity mismatch; Expected number of arguments does not match the given number \n expected: 1 \n given: " + arguments.size());
        }
    }

    public static SchemeExpression createListBuiltin(List<SchemeExpression> arguments) {
        return new ListExprNode(arguments);
    }

    public static SchemeExpression createConsBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() == 2) {
            return ConsExprNodeGen.create(arguments.get(0), arguments.get(1));
        } else {
            throw new SchemeException("cons: arity mismatch; Expected number of arguments does not match the given number \n expected: 2 \n given: " + arguments.size());
        }
    }

    public static SchemeExpression createCdrBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() == 1) {
            return CdrExprNodeGen.create(arguments.get(0));
        } else {
            throw new SchemeException("cdr: arity mismatch; Expected number of arguments does not match the given number \n expected: 1 \n given: " + arguments.size());
        }
    }

    public static SchemeExpression createCarBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() == 1) {
            return CarExprNodeGen.create(arguments.get(0));
        } else {
            throw new SchemeException("car: arity mismatch; Expected number of arguments does not match the given number \n expected: 1 \n given: " + arguments.size());
        }
    }

    public static SchemeExpression createLengthBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() == 1) {
            return LengthExprNodeGen.create(arguments.get(0));
        } else {
            throw new SchemeException("length: arity mismatch; Expected number of arguments does not match the given number \n expected: 1 \n given: " + arguments.size());
        }
    }

    public static SchemeExpression createAppendBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() > 0) {
            return new ReduceExprNode(AppendExprNodeGen.create(), arguments, SchemeCell.EMPTY_LIST);
        } else {
            //returning empty list
            return new ListExprNode(new ArrayList<>());
        }
    }

    public static SchemeExpression createMapBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() > 1) {
            return new MapExprNode(arguments.remove(0), arguments);
        } else {
            // -1 because first argument should be the function
            throw new SchemeException("map: arity mismatch; Expected number of argument does not match the given number \n expected: at least 2 \n given: " + (arguments.size()));
        }
    }

    public static SchemeExpression createLessThenOrEqual(List<SchemeExpression> arguments) {
        if (arguments.size() == 0) throw new SchemeException("<=: arity mismatch; Expected number of argument does not match the given number\nexpected: at least 1\ngiven: 0");
        if (arguments.size() == 1) return new BooleanLiteralNode(true);
        if (arguments.size() == 2) return LessThenEqualExprNodeGen.create(arguments.get(0), arguments.get(1));
        return new ReduceComparisonExprNode(reduceLessThenOrEqual(arguments), "<=");
    }

    private static List<SchemeExpression> reduceLessThenOrEqual(List<SchemeExpression> arguments) {
        List<SchemeExpression> result = new ArrayList<>();
        for (int i = 0; i < arguments.size() - 1; i++) {
            result.add(LessThenEqualExprNodeGen.create(arguments.get(i), arguments.get(i + 1)));
        }
        return result;
    }

    public static SchemeExpression createEqual(List<SchemeExpression> arguments) {
        if (arguments.size() == 0) throw new SchemeException("=: arity mismatch; Expected number of argument does not match the given number\nExpected: at least 1\nGiven: 0");
        if (arguments.size() == 1) return new BooleanLiteralNode(true);
        if (arguments.size() == 2) return EqualExprNodeGen.create(arguments.get(0), arguments.get(1));
        return new ReduceComparisonExprNode(reduceEqual(arguments), "=");
    }

    private static List<SchemeExpression> reduceEqual(List<SchemeExpression> arguments) {
        List<SchemeExpression> result = new ArrayList<>();
        for (int i = 0; i < arguments.size() - 1; i++) {
            result.add(EqualExprNodeGen.create(arguments.get(i), arguments.get(i + 1)));
        }
        return result;
    }

    public static SchemeExpression createLessThen(List<SchemeExpression> arguments) {
        if (arguments.size() == 0) throw new SchemeException("=: arity mismatch; Expected number of argument does not match the given number\nExpected: at least 1\nGiven: 0");
        if (arguments.size() == 1) return new BooleanLiteralNode(true);
        if (arguments.size() == 2) return LessThenExprNodeGen.create(arguments.get(0), arguments.get(1));
        return new ReduceComparisonExprNode(reduceLessThen(arguments), "<");
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
        throw new SchemeException("current-milliseconds: arity mismatch; Expected number of arguments does not match the given number\nExpected: 0\nGiven: " + arguments.size());
    }

    public static SchemeExpression createDisplayBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() == 1) {
            return DisplayExprNodeGen.create(arguments.get(0));
        }
        throw new SchemeException("display: arity mismatch; Expected number of arguments does not match the given number\nExpected: 1\nGiven: " + arguments.size());
    }

    public static SchemeExpression createNewlineBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() == 0) {
            return new NewlineExprNode();
        }
        throw new SchemeException("newline: arity mismatch; Expected number of arguments does not match the given number\nExpected: 0\nGiven: " + arguments.size());
    }
}
