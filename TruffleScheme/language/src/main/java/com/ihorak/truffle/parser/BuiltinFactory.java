package com.ihorak.truffle.parser;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.context.Context;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.ReduceExprNode;
import com.ihorak.truffle.node.exprs.builtin.CurrentMillisecondsExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.DisplayExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.EvalExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.NewlineExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.arithmetic.*;
import com.ihorak.truffle.node.exprs.builtin.list.*;
import com.ihorak.truffle.node.exprs.builtin.logical.EqualExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.logical.LessThenExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.logical.LessThenOrEqualExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.logical.ReduceComparisonExprNodeGen;
import com.ihorak.truffle.node.literals.BooleanLiteralNode;
import com.ihorak.truffle.node.literals.LongLiteralNode;
import com.ihorak.truffle.type.SchemeCell;

import java.util.ArrayList;
import java.util.List;

public class BuiltinFactory {

    public static SchemeExpression createDivideBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() > 0) {
            if (arguments.size() == 1) {
                return DivideOneArgumentExprNodeGen.create(arguments.get(0));
            } else {
                return ReduceDivideExprNodeGen.create(arguments.toArray(new SchemeExpression[0]), DivideExprNodeGen.create());
            }
        }
        throw new SchemeException("/: arity mismatch; Expected number of arguments does not match the given number \n expected: at least 1 \n given: 0");
    }

    public static SchemeExpression createMinusBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() > 0) {
            if (arguments.size() == 2) {
                return MinusTestNodeGen.create(arguments.get(0), arguments.get(1));
            }
            if (arguments.size() == 1) {
                return NegateNumberExprNodeGen.create(arguments.get(0));
            }
            return ReduceMinusExprNodeGen.create(arguments.toArray(new SchemeExpression[0]), MinusExprNodeGen.create());
        } else {
            throw new SchemeException("-: arity mismatch; Expected number of arguments does not match the given number \n expected: at least 1 \n given: 0");
        }
    }

    public static SchemeExpression createPlusBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() == 0) return new LongLiteralNode(0);
        if (arguments.size() == 1) return PlusTestNodeGen.create(new LongLiteralNode(0), arguments.get(0));
        return createPlusTree(arguments);
//        if (arguments.size() > 0) {
//            if (arguments.size() == 2) {
//                return PlusTestNodeGen.create(arguments.get(0), arguments.get(1));
//            }
//
//            return ReducePlusExprNodeGen.create(arguments.toArray(new SchemeExpression[0]), PlusExprNodeGen.create());
//        } else {
//            return new LongLiteralNode(0L);
//        }
    }

    public static SchemeExpression createPlusTree(List<SchemeExpression> arguments) {
        if (arguments.size() > 2) {
            return PlusTestNodeGen.create(arguments.remove(0), createPlusTree(arguments));
        } else {
            return PlusTestNodeGen.create(arguments.get(0), arguments.get(1));
        }
    }

    public static SchemeExpression createMultipleBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() > 0) {
            return ReduceMultiplyExprNodeGen.create(arguments.toArray(new SchemeExpression[0]), MultiplyExprNodeGen.create());
        } else {
            //number of arguments == 0 (return neutral element)
            return new LongLiteralNode(1);
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
            throw new SchemeException("map: arity mismatch; Expected number of argument does not match the given number \n expected: 1 \n given: " + (arguments.size() - 1));
        }
    }

    public static SchemeExpression createLessThenOrEqual(List<SchemeExpression> arguments) {
        if (arguments.size() > 0) {
            if (arguments.size() == 2) {
                return LessThenEqualTestNodeGen.create(arguments.get(0), arguments.get(1));
            }
            if (arguments.size() == 1) {
                return new BooleanLiteralNode(true);
            } else {
                return ReduceComparisonExprNodeGen.create(arguments.toArray(new SchemeExpression[0]), LessThenOrEqualExprNodeGen.create());
            }
        }
        throw new SchemeException("<=: arity mismatch; Expected number of argument does not match the given number \n expected: at least 1 \n given: 0");
    }

    public static SchemeExpression createEqual(List<SchemeExpression> arguments) {
        if (arguments.size() > 0) {
            if (arguments.size() == 1) {
                return new BooleanLiteralNode(true);
            } else {
                return ReduceComparisonExprNodeGen.create(arguments.toArray(new SchemeExpression[0]), EqualExprNodeGen.create());
            }
        }
        throw new SchemeException("=: arity mismatch; Expected number of argument does not match the given number\nExpected: at least 1\nGiven: 0");
    }

    public static SchemeExpression createLessThen(List<SchemeExpression> arguments) {
        if (arguments.size() > 0) {
            if (arguments.size() == 2) {
                return LessThenEqualTestNodeGen.create(arguments.get(0), arguments.get(1));
            }
            if (arguments.size() == 1) {
                return new BooleanLiteralNode(true);
            } else {
                return ReduceComparisonExprNodeGen.create(arguments.toArray(new SchemeExpression[0]), LessThenExprNodeGen.create());
            }
        }
        throw new SchemeException("=: arity mismatch; Expected number of argument does not match the given number\nExpected: at least 1\nGiven: 0");
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
            return NewlineExprNodeGen.create();
        }
        throw new SchemeException("newline: arity mismatch; Expected number of arguments does not match the given number\nExpected: 0\nGiven: " + arguments.size());
    }
}
