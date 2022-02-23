package com.ihorak.truffle.parser;

import com.ihorak.truffle.SchemeException;
import com.ihorak.truffle.node.EvalArgumentsNode;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.ReduceExprNode;
import com.ihorak.truffle.node.exprs.builtin.EvalExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.arithmetic.DivideExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.arithmetic.MinusExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.arithmetic.MultiplyExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.arithmetic.PlusExprNodeGen;
import com.ihorak.truffle.node.exprs.builtin.list.*;
import com.ihorak.truffle.node.exprs.builtin.logical.LessThenOrEqualExprNodeGen;
import com.ihorak.truffle.node.literals.LongLiteralNode;
import com.ihorak.truffle.type.SchemeCell;

import java.util.ArrayList;
import java.util.List;

public class BuiltinFactory {

    public static SchemeExpression createDivideBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() > 0) {
            return new ReduceExprNode(DivideExprNodeGen.create(), arguments, 1L);
        } else {
            throw new SchemeException("/: arity mismatch; Expected number of arguments does not match the given number \n expected: at least 1 \n given: 0");
        }
    }

    public static SchemeExpression createMinusBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() > 0) {
            return MinusExprNodeGen.create(new EvalArgumentsNode(arguments));
        } else {
            throw new SchemeException("-: arity mismatch; Expected number of arguments does not match the given number \n expected: at least 1 \n given: 0");
        }
    }

    public static SchemeExpression createPlusBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() > 0) {
            return PlusExprNodeGen.create(new EvalArgumentsNode(arguments));
        } else {
            //number of arguments == 0 (return neutral element)
            return new LongLiteralNode(0);
        }
    }

    public static SchemeExpression createLessThenOrEqual(List<SchemeExpression> arguments) {
        return LessThenOrEqualExprNodeGen.create(new EvalArgumentsNode(arguments));
    }

    public static SchemeExpression createMultipleBuiltin(List<SchemeExpression> arguments) {
        if (arguments.size() > 0) {
            return new ReduceExprNode(MultiplyExprNodeGen.create(), arguments, 1L);
        } else {
            //number of arguments == 0 (return neutral element)
            return new LongLiteralNode(1);
        }

    }

    public static SchemeExpression createEvalBuiltin(List<SchemeExpression> arguments, Context context) {
        if (arguments.size() == 1) {
            return EvalExprNodeGen.create(arguments.get(0), context);
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
            throw new SchemeException("map: arity mismatch; Expected number of argumnetsd does not match the given number \n expected: 1 \n given: " + (arguments.size() - 1));
        }
    }

    public static SchemeExpression test(List<SchemeExpression> arguments) {
        return new EvalArgumentsNode(arguments);
    }

}
