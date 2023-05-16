package com.ihorak.truffle.node.builtin.core.polyglot;

import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.ToJavaStringNode;
import com.ihorak.truffle.node.builtin.polyglot.ForeignToSchemeNode;
import com.ihorak.truffle.node.builtin.polyglot.TranslateInteropExceptionNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.library.CachedLibrary;

public class MemberNodes {

    public static final String HAS_MEMBERS = "has-members?";
    public static final String IS_MEMBER_READABLE = "member-readable?";
    public static final String IS_MEMBER_MODIFIABLE = "member-modifiable?";
    public static final String IS_MEMBER_INSERTABLE = "member-insertable?";
    public static final String IS_MEMBER_REMOVABLE = "member-removable?";
    public static final String IS_MEMBER_INVOCABLE = "member-invocable?";
    public static final String IS_MEMBER_WRITABLE = "member-writable?";
    public static final String IS_MEMBER_EXISTING = "member-existing?";
    public static final String GET_MEMBERS = "get-members";
    public static final String READ_MEMBER = "read-member";//.
    public static final String WRITE_MEMBER = "write-member";//set!
    public static final String REMOVE_MEMBER = "remove-member";//
    public static final String INVOKE_MEMBER = "invoke-member"; //.


    @GenerateUncached
    public static abstract class GetMembers extends SchemeNode {

        public abstract Object execute(Object receiver);

        @Specialization(limit = "getInteropCacheLimit()")
        protected Object getMembers(Object receiver,
                                    @Cached ForeignToSchemeNode foreignToSchemeNode,
                                    @Cached TranslateInteropExceptionNode translateInteropExceptionNode,
                                    @CachedLibrary("receiver") InteropLibrary interopLibrary) {
            try {
                var foreignObject = interopLibrary.getMembers(receiver);
                return foreignToSchemeNode.executeConvert(foreignObject);
            } catch (UnsupportedMessageException e) {
                throw translateInteropExceptionNode.execute(e, receiver);
            }
        }
    }


    @GenerateUncached
    public static abstract class HasMembers extends SchemeNode {

        public abstract boolean execute(Object receiver);

        @Specialization(limit = "getInteropCacheLimit()")
        protected boolean hasMembers(Object receiver, @CachedLibrary("receiver") InteropLibrary interopLibrary) {
            return interopLibrary.hasMembers(receiver);
        }
    }

    @GenerateUncached
    public static abstract class IsMemberReadable extends SchemeNode {

        public abstract boolean execute(Object receiver, Object identifier);

        @Specialization(limit = "getInteropCacheLimit()")
        protected boolean hasMembers(Object receiver,
                                     Object identifier,
                                     @CachedLibrary("receiver") InteropLibrary interopLibrary,
                                     @Cached ToJavaStringNode toJavaStringNode) {
            final var name = toJavaStringNode.execute(identifier);
            return interopLibrary.isMemberReadable(receiver, name);
        }
    }

    @GenerateUncached
    public static abstract class ReadMember extends SchemeNode {

        public abstract Object execute(Object receiver, Object identifier);

        @Specialization(limit = "getInteropCacheLimit()")
        protected Object readMember(Object receiver,
                                    Object identifier,
                                    @Cached ToJavaStringNode toJavaStringNode,
                                    @Cached ForeignToSchemeNode foreignToSchemeNode,
                                    @Cached TranslateInteropExceptionNode translateInteropExceptionNode,
                                    @CachedLibrary("receiver") InteropLibrary interopLibrary) {
            try {
                final var name = toJavaStringNode.execute(identifier);
                var foreignObject = interopLibrary.readMember(receiver, name);
                return foreignToSchemeNode.executeConvert(foreignObject);
            } catch (InteropException e) {
                throw translateInteropExceptionNode.execute(e, receiver);
            }
        }
    }


    @GenerateUncached
    public static abstract class IsMemberModifiable extends SchemeNode {

        public abstract boolean execute(Object receiver, Object identifier);

        @Specialization(limit = "getInteropCacheLimit()")
        protected boolean isMemberModifiable(Object receiver,
                                             Object identifier,
                                             @Cached ToJavaStringNode toJavaStringNode,
                                             @CachedLibrary("receiver") InteropLibrary interopLibrary) {
            final var name = toJavaStringNode.execute(identifier);
            return interopLibrary.isMemberModifiable(receiver, name);
        }
    }

    @GenerateUncached
    public static abstract class IsMemberInsertable extends SchemeNode {

        public abstract boolean execute(Object receiver, Object identifier);

        @Specialization(limit = "getInteropCacheLimit()")
        protected boolean isMemberInsertable(Object receiver,
                                             Object identifier,
                                             @Cached ToJavaStringNode toJavaStringNode,
                                             @CachedLibrary("receiver") InteropLibrary interopLibrary) {
            final var name = toJavaStringNode.execute(identifier);
            return interopLibrary.isMemberInsertable(receiver, name);
        }
    }


    @GenerateUncached
    public static abstract class WriteMember extends SchemeNode {

        public abstract void execute(Object receiver, Object identifier, Object value);

        @Specialization(limit = "getInteropCacheLimit()")
        protected void writeMember(Object receiver,
                                   Object identifier,
                                   Object value,
                                   @Cached ToJavaStringNode toJavaStringNode,
                                   @Cached TranslateInteropExceptionNode translateInteropExceptionNode,
                                   @CachedLibrary("receiver") InteropLibrary interopLibrary) {
            try {
                final var name = toJavaStringNode.execute(identifier);
                interopLibrary.writeMember(receiver, name, value);
            } catch (InteropException exception) {
                throw translateInteropExceptionNode.execute(exception, receiver);
            }
        }
    }

    @GenerateUncached
    public static abstract class IsMemberRemovable extends SchemeNode {

        public abstract boolean execute(Object receiver, Object identifier);

        @Specialization(limit = "getInteropCacheLimit()")
        protected boolean isMemberRemovable(Object receiver,
                                            Object identifier,
                                            @Cached ToJavaStringNode toJavaStringNode,
                                            @CachedLibrary("receiver") InteropLibrary interopLibrary) {
            final var name = toJavaStringNode.execute(identifier);
            return interopLibrary.isMemberRemovable(receiver, name);
        }
    }

    @GenerateUncached
    public static abstract class RemoveMember extends SchemeNode {

        public abstract void execute(Object receiver, Object identifier);

        @Specialization(limit = "getInteropCacheLimit()")
        protected void removeMember(Object receiver,
                                    Object identifier,
                                    @Cached ToJavaStringNode toJavaStringNode,
                                    @Cached TranslateInteropExceptionNode translateInteropExceptionNode,
                                    @CachedLibrary("receiver") InteropLibrary interopLibrary) {
            try {
                final var name = toJavaStringNode.execute(identifier);
                interopLibrary.removeMember(receiver, name);
            } catch (InteropException exception) {
                throw translateInteropExceptionNode.execute(exception, receiver);
            }
        }
    }

    @GenerateUncached
    public static abstract class IsMemberInvocable extends SchemeNode {

        public abstract boolean execute(Object receiver, Object identifier);

        @Specialization(limit = "getInteropCacheLimit()")
        protected boolean isMemberInvocable(Object receiver,
                                            Object identifier,
                                            @Cached ToJavaStringNode toJavaStringNode,
                                            @CachedLibrary("receiver") InteropLibrary interopLibrary) {
            final var name = toJavaStringNode.execute(identifier);
            return interopLibrary.isMemberInvocable(receiver, name);
        }
    }

    @GenerateUncached
    public static abstract class InvokeMember extends SchemeNode {

        public abstract Object execute(Object receiver, Object identifier, Object[] args);

        @Specialization(limit = "getInteropCacheLimit()")
        protected Object invokeMember(Object receiver,
                                      Object identifier,
                                      Object[] args,
                                      @Cached ToJavaStringNode toJavaStringNode,
                                      @Cached ForeignToSchemeNode foreignToSchemeNode,
                                      @Cached TranslateInteropExceptionNode translateInteropExceptionNode,
                                      @CachedLibrary("receiver") InteropLibrary interopLibrary) {
            try {
                final var name = toJavaStringNode.execute(identifier);
                var foreign = interopLibrary.invokeMember(receiver, name, args);
                return foreignToSchemeNode.executeConvert(foreign);
            } catch (InteropException exception) {
                throw translateInteropExceptionNode.execute(exception, receiver);
            }
        }
    }

    @GenerateUncached
    public static abstract class IsMemberWritable extends SchemeNode {

        public abstract boolean execute(Object receiver, Object identifier);

        @Specialization(limit = "getInteropCacheLimit()")
        protected boolean isMemberWritable(Object receiver,
                                           Object identifier,
                                           @Cached ToJavaStringNode toJavaStringNode,
                                           @CachedLibrary("receiver") InteropLibrary interopLibrary) {
            final var name = toJavaStringNode.execute(identifier);
            return interopLibrary.isMemberWritable(receiver, name);
        }
    }

    @GenerateUncached
    public static abstract class IsMemberExisting extends SchemeNode {

        public abstract boolean execute(Object receiver, Object identifier);

        @Specialization(limit = "getInteropCacheLimit()")
        protected boolean isMemberExisting(Object receiver,
                                           Object identifier,
                                           @Cached ToJavaStringNode toJavaStringNode,
                                           @CachedLibrary("receiver") InteropLibrary interopLibrary) {
            final var name = toJavaStringNode.execute(identifier);
            return interopLibrary.isMemberExisting(receiver, name);
        }
    }
}
