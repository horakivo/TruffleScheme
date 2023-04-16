package com.ihorak.truffle.convertor;


import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.polyglot.ConvertSchemeExprsArgumentsNode;
import com.ihorak.truffle.node.polyglot.EvalSourceExprNodeGen;
import com.ihorak.truffle.node.polyglot.MemberNodesFactory;
import com.ihorak.truffle.node.polyglot.ReadForeignGlobalScopeExprNodeGen;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.ihorak.truffle.convertor.callable.BuiltinConverter.*;
import static com.ihorak.truffle.node.polyglot.MemberNodes.*;

public class BuiltinFactory {


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

    public static SchemeExpression createGetMembers(List<SchemeExpression> arguments, @Nullable ParserRuleContext getMembersCtx) {
        if (arguments.size() == 1) {
            var getMembersExpr = MemberNodesFactory.GetMembersNodeGen.create(arguments.get(0));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(getMembersExpr, getMembersCtx);
        }
        throw ConverterException.arityException(GET_MEMBERS, 1, arguments.size());
    }

    public static SchemeExpression createHasMembers(List<SchemeExpression> arguments, @Nullable ParserRuleContext hasMembersCtx) {
        if (arguments.size() == 1) {
            var getMembersExpr = MemberNodesFactory.HasMembersNodeGen.create(arguments.get(0));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(getMembersExpr, hasMembersCtx);
        }
        throw ConverterException.arityException(HAS_MEMBERS, 1, arguments.size());
    }

    public static SchemeExpression createIsMemberReadable(List<SchemeExpression> arguments, @Nullable ParserRuleContext isMemberReadableCtx) {
        if (arguments.size() == 2) {
            var getMembersExpr = MemberNodesFactory.IsMemberReadableNodeGen.create(arguments.get(0), arguments.get(1));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(getMembersExpr, isMemberReadableCtx);
        }
        throw ConverterException.arityException(IS_MEMBER_READABLE, 2, arguments.size());
    }

    public static SchemeExpression createReadMember(List<SchemeExpression> arguments, @Nullable ParserRuleContext readMemberCtx) {
        if (arguments.size() == 2) {
            var getMembersExpr = MemberNodesFactory.ReadMemberExprNodeGen.create(arguments.get(0), arguments.get(1));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(getMembersExpr, readMemberCtx);
        }
        throw ConverterException.arityException(READ_MEMBER, 2, arguments.size());
    }

    public static SchemeExpression createIsMemberModifiable(List<SchemeExpression> arguments, @Nullable ParserRuleContext isMemberModifiableCtx) {
        if (arguments.size() == 2) {
            var getMembersExpr = MemberNodesFactory.IsMemberModifiableNodeGen.create(arguments.get(0), arguments.get(1));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(getMembersExpr, isMemberModifiableCtx);
        }
        throw ConverterException.arityException(IS_MEMBER_MODIFIABLE, 2, arguments.size());
    }


    public static SchemeExpression createIsMemberInsertable(List<SchemeExpression> arguments, @Nullable ParserRuleContext isMemberInsertableCtx) {
        if (arguments.size() == 2) {
            var expr = MemberNodesFactory.IsMemberInsertableNodeGen.create(arguments.get(0), arguments.get(1));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(expr, isMemberInsertableCtx);
        }
        throw ConverterException.arityException(IS_MEMBER_INSERTABLE, 2, arguments.size());
    }

    public static SchemeExpression createWriteMember(List<SchemeExpression> arguments, @Nullable ParserRuleContext writeMemberCtx) {
        if (arguments.size() == 3) {
            var expr = MemberNodesFactory.WriteMemberNodeGen.create(arguments.get(0), arguments.get(1), arguments.get(2));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(expr, writeMemberCtx);
        }
        throw ConverterException.arityException(WRITE_MEMBER, 3, arguments.size());
    }

    public static SchemeExpression createIsMemberRemovable(List<SchemeExpression> arguments, @Nullable ParserRuleContext isMemberRemovableCtx) {
        if (arguments.size() == 2) {
            var expr = MemberNodesFactory.IsMemberRemovableNodeGen.create(arguments.get(0), arguments.get(1));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(expr, isMemberRemovableCtx);
        }
        throw ConverterException.arityException(IS_MEMBER_REMOVABLE, 2, arguments.size());
    }

    public static SchemeExpression createRemoveMember(List<SchemeExpression> arguments, @Nullable ParserRuleContext removeMemberCtx) {
        if (arguments.size() == 2) {
            var expr = MemberNodesFactory.RemoveMemberNodeGen.create(arguments.get(0), arguments.get(1));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(expr, removeMemberCtx);
        }
        throw ConverterException.arityException(REMOVE_MEMBER, 2, arguments.size());
    }

    public static SchemeExpression createIsMemberInvocable(List<SchemeExpression> arguments, @Nullable ParserRuleContext isMemberInvocableCtx) {
        if (arguments.size() == 2) {
            var expr = MemberNodesFactory.IsMemberInvocableNodeGen.create(arguments.get(0), arguments.get(1));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(expr, isMemberInvocableCtx);
        }
        throw ConverterException.arityException(IS_MEMBER_INVOCABLE, 2, arguments.size());
    }

    public static SchemeExpression createInvokeMember(List<SchemeExpression> arguments, @Nullable ParserRuleContext invokeMemberCtx) {
        if (arguments.size() >= 2) {
            var argsList = arguments.subList(2, arguments.size());
            var argumentsToObjectArrayExpr = new ConvertSchemeExprsArgumentsNode(argsList);
            var expr = MemberNodesFactory.InvokeMemberNodeGen.create(arguments.get(0), arguments.get(1), argumentsToObjectArrayExpr);
            return SourceSectionUtil.setSourceSectionAndReturnExpr(expr, invokeMemberCtx);
        }
        throw ConverterException.arityException(INVOKE_MEMBER, 3, arguments.size());
    }

    public static SchemeExpression createIsMemberWritable(List<SchemeExpression> arguments, @Nullable ParserRuleContext isMemberWritableCtx) {
        if (arguments.size() == 2) {
            var expr = MemberNodesFactory.IsMemberWritableNodeGen.create(arguments.get(0), arguments.get(1));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(expr, isMemberWritableCtx);
        }
        throw ConverterException.arityException(IS_MEMBER_WRITABLE, 2, arguments.size());
    }

    public static SchemeExpression createIsMemberExisting(List<SchemeExpression> arguments, @Nullable ParserRuleContext isMemberExistingCtx) {
        if (arguments.size() == 2) {
            var expr = MemberNodesFactory.IsMemberExistingNodeGen.create(arguments.get(0), arguments.get(1));
            return SourceSectionUtil.setSourceSectionAndReturnExpr(expr, isMemberExistingCtx);
        }
        throw ConverterException.arityException(IS_MEMBER_EXISTING, 2, arguments.size());
    }



}
