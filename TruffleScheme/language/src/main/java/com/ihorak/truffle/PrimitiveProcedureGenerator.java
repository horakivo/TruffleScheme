package com.ihorak.truffle;

import com.ihorak.truffle.node.builtin.ApplyBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.CurrentMillisBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.DisplayBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.IsNullBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.MapBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.ModuloBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.NotBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.arithmetic.DivideBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.arithmetic.MinusBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.arithmetic.MultiplyBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.arithmetic.PlusBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.comparison.EqualBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.comparison.EqualNumbersBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.comparison.LessThanBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.comparison.LessThanEqualBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.comparison.MoreThanBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.comparison.MoreThanEqualBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.list.AppendBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.list.CarBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.list.CdrBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.list.ConsBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.list.LengthBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.list.ListBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.polyglot.GetMembersBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.polyglot.HasMembersBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.polyglot.InvokeMemberBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.polyglot.IsMemberExistingBuiltinNode;
import com.ihorak.truffle.node.builtin.polyglot.IsMemberExistingBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.polyglot.IsMemberInsertableBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.polyglot.IsMemberInvocableBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.polyglot.IsMemberModifiableBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.polyglot.IsMemberReadableBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.polyglot.IsMemberRemovableBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.polyglot.IsMemberWritableBuiltinNode;
import com.ihorak.truffle.node.builtin.polyglot.IsMemberWritableBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.polyglot.ReadMemberBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.polyglot.RemoveMemberBuiltinNodeFactory;
import com.ihorak.truffle.node.builtin.polyglot.WriteMemberBuiltinNodeFactory;
import com.ihorak.truffle.runtime.PrimitiveProcedure;
import com.ihorak.truffle.runtime.SchemeSymbol;

import java.util.HashMap;
import java.util.Map;

import static com.ihorak.truffle.node.builtin.core.polyglot.MemberNodes.*;

public class PrimitiveProcedureGenerator {


    public static Map<SchemeSymbol, Object> generate() {
        HashMap<SchemeSymbol, Object> result = new HashMap<>();


        var plusPrimitiveProcedure = new PrimitiveProcedure("+", PlusBuiltinNodeFactory.getInstance());
        var minusPrimitiveProcedure = new PrimitiveProcedure("-", MinusBuiltinNodeFactory.getInstance());
        var multiplyPrimitiveProcedure = new PrimitiveProcedure("*", MultiplyBuiltinNodeFactory.getInstance());
        var dividePrimaryProcedure = new PrimitiveProcedure("/", DivideBuiltinNodeFactory.getInstance());


        var listPrimitiveProcedure = new PrimitiveProcedure("list", ListBuiltinNodeFactory.getInstance());
        var mapPrimitiveProcedure = new PrimitiveProcedure("map", MapBuiltinNodeFactory.getInstance());
        var appendPrimitiveProcedure = new PrimitiveProcedure("append", AppendBuiltinNodeFactory.getInstance());

        var equalPrimitiveProcedure = new PrimitiveProcedure("equal?", EqualBuiltinNodeFactory.getInstance());
        var equalNumbersPrimitiveProcedure = new PrimitiveProcedure("=", EqualNumbersBuiltinNodeFactory.getInstance());
        var moreThenPrimitiveProcedure = new PrimitiveProcedure(">", MoreThanBuiltinNodeFactory.getInstance());
        var moreThenEqualPrimitiveProcedure = new PrimitiveProcedure(">=", MoreThanEqualBuiltinNodeFactory.getInstance());
        var lessThenEqualPrimitiveProcedure = new PrimitiveProcedure("<=", LessThanEqualBuiltinNodeFactory.getInstance());
        var lessThenPrimitiveProcedure = new PrimitiveProcedure("<", LessThanBuiltinNodeFactory.getInstance());

        var moduloPrimitiveProcedure = new PrimitiveProcedure("modulo", ModuloBuiltinNodeFactory.getInstance());
        var isNullPrimitiveProcedure = new PrimitiveProcedure("null?", IsNullBuiltinNodeFactory.getInstance());
        var notPrimitiveProcedure = new PrimitiveProcedure("not", NotBuiltinNodeFactory.getInstance());
        var displayPrimitiveProcedure = new PrimitiveProcedure("display", DisplayBuiltinNodeFactory.getInstance());
        var currentMillisPrimitiveProcedure = new PrimitiveProcedure("current-milliseconds", CurrentMillisBuiltinNodeFactory.getInstance());
        var applyPrimitiveProcedure = new PrimitiveProcedure("apply", ApplyBuiltinNodeFactory.getInstance());

        var carPrimitiveProcedure = new PrimitiveProcedure("car", CarBuiltinNodeFactory.getInstance());
        var consPrimitiveProcedure = new PrimitiveProcedure("cons", ConsBuiltinNodeFactory.getInstance());
        var lengthPrimitiveProcedure = new PrimitiveProcedure("length", LengthBuiltinNodeFactory.getInstance());
        var cdrPrimitiveProcedure = new PrimitiveProcedure("cdr", CdrBuiltinNodeFactory.getInstance());

        //polyglot
        var getMembersPrimitive = new PrimitiveProcedure(GET_MEMBERS, GetMembersBuiltinNodeFactory.getInstance());
        var hasMembersPrimitive = new PrimitiveProcedure(HAS_MEMBERS, HasMembersBuiltinNodeFactory.getInstance());
        var isMemberReadablePrimitive = new PrimitiveProcedure(IS_MEMBER_READABLE, IsMemberReadableBuiltinNodeFactory.getInstance());
        var readMemberPrimitive = new PrimitiveProcedure(READ_MEMBER, ReadMemberBuiltinNodeFactory.getInstance());
        var isMemberModifiablePrimitive = new PrimitiveProcedure(IS_MEMBER_MODIFIABLE, IsMemberModifiableBuiltinNodeFactory.getInstance());
        var isMemberInsertablePrimitive = new PrimitiveProcedure(IS_MEMBER_INSERTABLE, IsMemberInsertableBuiltinNodeFactory.getInstance());
        var writeMemberPrimitive = new PrimitiveProcedure(WRITE_MEMBER, WriteMemberBuiltinNodeFactory.getInstance());
        var isMemberRemovablePrimitive = new PrimitiveProcedure(IS_MEMBER_REMOVABLE, IsMemberRemovableBuiltinNodeFactory.getInstance());
        var removeMemberPrimitive = new PrimitiveProcedure(REMOVE_MEMBER, RemoveMemberBuiltinNodeFactory.getInstance());
        var isMemberInvocablePrimitive = new PrimitiveProcedure(IS_MEMBER_INVOCABLE, IsMemberInvocableBuiltinNodeFactory.getInstance());
        var invokeMemberPrimitive = new PrimitiveProcedure(INVOKE_MEMBER, InvokeMemberBuiltinNodeFactory.getInstance());
        var isMemberWritablePrimitive = new PrimitiveProcedure(IS_MEMBER_WRITABLE, IsMemberWritableBuiltinNodeFactory.getInstance());
        var isMemberExistingPrimitive = new PrimitiveProcedure(IS_MEMBER_EXISTING, IsMemberExistingBuiltinNodeFactory.getInstance());


        result.put(new SchemeSymbol("+"), plusPrimitiveProcedure);
        result.put(new SchemeSymbol("-"), minusPrimitiveProcedure);
        result.put(new SchemeSymbol("*"), multiplyPrimitiveProcedure);
        result.put(new SchemeSymbol("/"), dividePrimaryProcedure);
        result.put(new SchemeSymbol("car"), carPrimitiveProcedure);
        result.put(new SchemeSymbol("cons"), consPrimitiveProcedure);
        result.put(new SchemeSymbol("length"), lengthPrimitiveProcedure);
        result.put(new SchemeSymbol("list"), listPrimitiveProcedure);
        result.put(new SchemeSymbol("cdr"), cdrPrimitiveProcedure);
        result.put(new SchemeSymbol("equal?"), equalPrimitiveProcedure);
        result.put(new SchemeSymbol("="), equalNumbersPrimitiveProcedure);
        result.put(new SchemeSymbol(">"), moreThenPrimitiveProcedure);
        result.put(new SchemeSymbol(">="), moreThenEqualPrimitiveProcedure);
        result.put(new SchemeSymbol("<"), lessThenPrimitiveProcedure);
        result.put(new SchemeSymbol("<="), lessThenEqualPrimitiveProcedure);
        result.put(new SchemeSymbol("modulo"), moduloPrimitiveProcedure);
        result.put(new SchemeSymbol("null?"), isNullPrimitiveProcedure);
        result.put(new SchemeSymbol("not"), notPrimitiveProcedure);
        result.put(new SchemeSymbol("display"), displayPrimitiveProcedure);
        result.put(new SchemeSymbol("current-milliseconds"), currentMillisPrimitiveProcedure);
        result.put(new SchemeSymbol("append"), appendPrimitiveProcedure);
        result.put(new SchemeSymbol("apply"), applyPrimitiveProcedure);
        result.put(new SchemeSymbol("map"), mapPrimitiveProcedure);
        //polyglot
        result.put(new SchemeSymbol(GET_MEMBERS), getMembersPrimitive);
        result.put(new SchemeSymbol(HAS_MEMBERS), hasMembersPrimitive);
        result.put(new SchemeSymbol(IS_MEMBER_READABLE), isMemberReadablePrimitive);
        result.put(new SchemeSymbol(READ_MEMBER), readMemberPrimitive);
        result.put(new SchemeSymbol(IS_MEMBER_MODIFIABLE), isMemberModifiablePrimitive);
        result.put(new SchemeSymbol(IS_MEMBER_INSERTABLE), isMemberInsertablePrimitive);
        result.put(new SchemeSymbol(WRITE_MEMBER), writeMemberPrimitive);
        result.put(new SchemeSymbol(IS_MEMBER_REMOVABLE), isMemberRemovablePrimitive);
        result.put(new SchemeSymbol(REMOVE_MEMBER), removeMemberPrimitive);
        result.put(new SchemeSymbol(IS_MEMBER_INVOCABLE), isMemberInvocablePrimitive);
        result.put(new SchemeSymbol(INVOKE_MEMBER), invokeMemberPrimitive);
        result.put(new SchemeSymbol(IS_MEMBER_WRITABLE), isMemberWritablePrimitive);
        result.put(new SchemeSymbol(IS_MEMBER_EXISTING), isMemberExistingPrimitive);

        return result;
    }
}
