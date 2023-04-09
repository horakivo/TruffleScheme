package com.ihorak.truffle.node.polyglot;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.interop.ForeignToSchemeNode;
import com.ihorak.truffle.type.SchemeList;
import com.ihorak.truffle.type.UndefinedValue;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.strings.TruffleString;

public class MemberNodes {

    public static final String HAS_MEMBERS = "has-members?";
    public static final String IS_MEMBER_READABLE = "is-member-readable?";
    public static final String IS_MEMBER_MODIFIABLE = "is-member-modifiable?";
    public static final String IS_MEMBER_INSERTABLE = "is-member-insertable?";
    public static final String IS_MEMBER_REMOVABLE = "is-member-removable?";
    public static final String IS_MEMBER_INVOCABLE = "is-member-invocable?";
    public static final String IS_MEMBER_WRITABLE = "is-member-writable?";
    public static final String IS_MEMBER_EXISTING = "is-member-existing?";
    public static final String GET_MEMBERS = "get-members";
    public static final String READ_MEMBER = "read-member";
    public static final String WRITE_MEMBER = "write-member";
    public static final String REMOVE_MEMBER = "remove-member";
    public static final String INVOKE_MEMBER = "invoke-member";


    @NodeChild("receiver")
    public static abstract class GetMembers extends SchemeExpression {

        @Specialization(limit = "getInteropCacheLimit()")
        protected Object getMembers(Object receiver,
                                    @Cached ForeignToSchemeNode foreignToSchemeNode,
                                    @Cached TranslateInteropExceptionNode translateInteropExceptionNode,
                                    @CachedLibrary("receiver") InteropLibrary interopLibrary) {
            try {
                var result = interopLibrary.getMembers(receiver);
                return foreignToSchemeNode.executeConvert(result);
            } catch (UnsupportedMessageException e) {
                throw translateInteropExceptionNode.execute(e, receiver, GET_MEMBERS, null);
            }
        }
    }


    @NodeChild("receiver")
    public static abstract class HasMembers extends SchemeExpression {

        @Specialization(limit = "getInteropCacheLimit()")
        protected boolean hasMembers(Object receiver,
                                     @CachedLibrary("receiver") InteropLibrary interopLibrary) {
            return interopLibrary.hasMembers(receiver);
        }
    }

    @NodeChild("receiver")
    @NodeChild("member")
    public static abstract class IsMemberReadable extends SchemeExpression {

        @Specialization(limit = "getInteropCacheLimit()")
        protected boolean hasMembers(Object receiver, TruffleString member,
                                     @CachedLibrary("receiver") InteropLibrary interopLibrary,
                                     @Cached TruffleString.ToJavaStringNode toJavaStringNode) {
            return interopLibrary.isMemberReadable(receiver, toJavaStringNode.execute(member));
        }

        @Fallback
        protected Object fallback(Object obj, Object identifier) {
            throw PolyglotException.wrongMessageIdentifierType(IS_MEMBER_READABLE, identifier, this);
        }
    }

    public static abstract class ReadMemberNode extends SchemeNode {

        public abstract Object execute(Object foreignObject, TruffleString identifier);

        @Specialization(limit = "getInteropCacheLimit()")
        protected Object readMember(Object foreignObject,
                                    TruffleString identifier,
                                    @Cached TruffleString.ToJavaStringNode toJavaStringNode,
                                    @Cached ForeignToSchemeNode foreignToSchemeNode,
                                    @Cached TranslateInteropExceptionNode translateInteropExceptionNode,
                                    @CachedLibrary("foreignObject") InteropLibrary interopLibrary) {
            try {
                var result = interopLibrary.readMember(foreignObject, toJavaStringNode.execute(identifier));
                return foreignToSchemeNode.executeConvert(result);
            } catch (InteropException e) {
                throw translateInteropExceptionNode.execute(e, foreignObject, READ_MEMBER, null);
            }
        }
    }

    @NodeChild("foreignObject")
    @NodeChild("identifier")
    public static abstract class ReadMemberExprNode extends SchemeExpression {


        @Specialization
        protected Object readMember(Object foreignObject, TruffleString identifier, @Cached ReadMemberNode readMemberNode) {
            return readMemberNode.execute(foreignObject, identifier);
        }

        @Fallback
        protected Object fallback(Object obj, Object identifier) {
            throw PolyglotException.wrongMessageIdentifierType(READ_MEMBER, identifier, this);
        }
    }

    @NodeChild("foreignObject")
    @NodeChild("identifier")
    public static abstract class IsMemberModifiable extends SchemeExpression {

        @Specialization(limit = "getInteropCacheLimit()")
        protected boolean isMemberModifiable(Object receiver,
                                             TruffleString identifier,
                                             @Cached TruffleString.ToJavaStringNode toJavaStringNode,
                                             @CachedLibrary("receiver") InteropLibrary interopLibrary) {
            return interopLibrary.isMemberModifiable(receiver, toJavaStringNode.execute(identifier));
        }

        @Fallback
        protected Object fallback(Object obj, Object identifier) {
            throw PolyglotException.wrongMessageIdentifierType(IS_MEMBER_MODIFIABLE, identifier, this);
        }
    }

    @NodeChild("foreignObject")
    @NodeChild("identifier")
    public static abstract class IsMemberInsertable extends SchemeExpression {

        @Specialization(limit = "getInteropCacheLimit()")
        protected boolean isMemberInsertable(Object receiver,
                                             TruffleString identifier,
                                             @Cached TruffleString.ToJavaStringNode toJavaStringNode,
                                             @CachedLibrary("receiver") InteropLibrary interopLibrary) {
            return interopLibrary.isMemberInsertable(receiver, toJavaStringNode.execute(identifier));
        }

        @Fallback
        protected Object fallback(Object obj, Object identifier) {
            throw PolyglotException.wrongMessageIdentifierType(IS_MEMBER_INSERTABLE, identifier, this);
        }
    }


    @NodeChild("foreignObject")
    @NodeChild("identifier")
    @NodeChild("value")
    public static abstract class WriteMember extends SchemeExpression {

        @Specialization(limit = "getInteropCacheLimit()")
        protected Object writeMember(Object receiver,
                                     TruffleString identifier,
                                     Object value,
                                     @Cached TruffleString.ToJavaStringNode toJavaStringNode,
                                     @Cached TranslateInteropExceptionNode translateInteropExceptionNode,
                                     @CachedLibrary("receiver") InteropLibrary interopLibrary) {
            try {

                interopLibrary.writeMember(receiver, toJavaStringNode.execute(identifier), value);
                return UndefinedValue.SINGLETON;
            } catch (InteropException exception) {
                throw translateInteropExceptionNode.execute(exception, receiver, WRITE_MEMBER, null);
            }
        }

        @Fallback
        protected Object fallback(Object obj, Object identifier, Object value) {
            throw PolyglotException.wrongMessageIdentifierType(WRITE_MEMBER, identifier, this);
        }
    }

    @NodeChild("foreignObject")
    @NodeChild("identifier")
    public static abstract class IsMemberRemovable extends SchemeExpression {

        @Specialization(limit = "getInteropCacheLimit()")
        protected boolean isMemberRemovable(Object receiver,
                                            TruffleString identifier,
                                            @Cached TruffleString.ToJavaStringNode toJavaStringNode,
                                            @CachedLibrary("receiver") InteropLibrary interopLibrary) {
            return interopLibrary.isMemberRemovable(receiver, toJavaStringNode.execute(identifier));
        }

        @Fallback
        protected Object fallback(Object obj, Object identifier) {
            throw PolyglotException.wrongMessageIdentifierType(IS_MEMBER_REMOVABLE, identifier, this);
        }
    }

    @NodeChild("foreignObject")
    @NodeChild("identifier")
    public static abstract class RemoveMember extends SchemeExpression {

        @Specialization(limit = "getInteropCacheLimit()")
        protected Object removeMember(Object receiver,
                                      TruffleString identifier,
                                      @Cached TruffleString.ToJavaStringNode toJavaStringNode,
                                      @Cached TranslateInteropExceptionNode translateInteropExceptionNode,
                                      @CachedLibrary("receiver") InteropLibrary interopLibrary) {
            try {

                interopLibrary.removeMember(receiver, toJavaStringNode.execute(identifier));
                return UndefinedValue.SINGLETON;
            } catch (InteropException exception) {
                throw translateInteropExceptionNode.execute(exception, receiver, REMOVE_MEMBER, null);
            }
        }

        @Fallback
        protected Object fallback(Object obj, Object identifier) {
            throw PolyglotException.wrongMessageIdentifierType(REMOVE_MEMBER, identifier, this);
        }
    }

    @NodeChild("foreignObject")
    @NodeChild("identifier")
    public static abstract class IsMemberInvocable extends SchemeExpression {

        @Specialization(limit = "getInteropCacheLimit()")
        protected boolean isMemberInvocable(Object receiver,
                                            TruffleString identifier,
                                            @Cached TruffleString.ToJavaStringNode toJavaStringNode,
                                            @CachedLibrary("receiver") InteropLibrary interopLibrary) {
            return interopLibrary.isMemberInvocable(receiver, toJavaStringNode.execute(identifier));
        }

        @Fallback
        protected Object fallback(Object obj, Object identifier) {
            throw PolyglotException.wrongMessageIdentifierType(IS_MEMBER_INVOCABLE, identifier, this);
        }
    }

    @NodeChild("foreignObject")
    @NodeChild("identifier")
    @NodeChild("arguments")
    public static abstract class InvokeMember extends SchemeExpression {


        @Specialization(limit = "getInteropCacheLimit()")
        protected Object invokeMember(Object receiver,
                                      TruffleString identifier,
                                      Object[] args,
                                      @Cached TruffleString.ToJavaStringNode toJavaStringNode,
                                      @Cached ForeignToSchemeNode foreignToSchemeNode,
                                      @Cached TranslateInteropExceptionNode translateInteropExceptionNode,
                                      @CachedLibrary("receiver") InteropLibrary interopLibrary) {
            try {
                var result = interopLibrary.invokeMember(receiver, toJavaStringNode.execute(identifier), args);
                return foreignToSchemeNode.executeConvert(result);
            } catch (InteropException exception) {
                throw translateInteropExceptionNode.execute(exception, receiver, INVOKE_MEMBER, args);
            }
        }


        @Fallback
        protected Object fallback(Object obj, Object identifier, Object arguments) {
            throw PolyglotException.wrongMessageIdentifierType(INVOKE_MEMBER, identifier, this);
        }
    }

    @NodeChild("foreignObject")
    @NodeChild("identifier")
    public static abstract class IsMemberWritable extends SchemeExpression {

        @Specialization(limit = "getInteropCacheLimit()")
        protected boolean isMemberWritable(Object receiver,
                                           TruffleString identifier,
                                           @Cached TruffleString.ToJavaStringNode toJavaStringNode,
                                           @CachedLibrary("receiver") InteropLibrary interopLibrary) {
            return interopLibrary.isMemberWritable(receiver, toJavaStringNode.execute(identifier));
        }

        @Fallback
        protected Object fallback(Object obj, Object identifier) {
            throw PolyglotException.wrongMessageIdentifierType(IS_MEMBER_WRITABLE, identifier, this);
        }
    }

    @NodeChild("foreignObject")
    @NodeChild("identifier")
    public static abstract class IsMemberExisting extends SchemeExpression {

        @Specialization(limit = "getInteropCacheLimit()")
        protected boolean isMemberExisting(Object receiver,
                                           TruffleString identifier,
                                           @Cached TruffleString.ToJavaStringNode toJavaStringNode,
                                           @CachedLibrary("receiver") InteropLibrary interopLibrary) {
            return interopLibrary.isMemberExisting(receiver, toJavaStringNode.execute(identifier));
        }

        @Fallback
        protected Object fallback(Object obj, Object identifier) {
            throw PolyglotException.wrongMessageIdentifierType(IS_MEMBER_EXISTING, identifier, this);
        }
    }
}
