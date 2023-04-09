import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MemberNodesTest {

    private Context context;

    @Before
    public void setUp() {
        this.context = Context.newBuilder().allowAllAccess(true).build();
    }

    @After
    public void tearDown() {
        this.context.close();
    }


    @Test
    public void hasMembers() {
        var program = """
                (define object (eval-source "js" "a = { id: 2, method: x => x+1 }"))
                (has-members? object)
                """;

        var result = context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void getMembers() {
        var program = """
                (define object (eval-source "js" "a = { id: 2, method: x => x+1 }"))
                (get-members object)
                """;

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals("id", result.getArrayElement(0).asString());
        assertEquals("method", result.getArrayElement(1).asString());
    }

    @Test
    public void IsMemberReadable() {
        var program = """
                (define object (eval-source "js" "a = { id: 2, method: x => x+1 }"))
                (is-member-readable? object "id")
                """;

        var result = context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void IsMemberModifiable() {
        var program = """
                (define object (eval-source "js" "a = { id: 2, method: x => x+1 }"))
                (is-member-modifiable? object "id")
                """;

        var result = context.eval("scm", program);

        assertTrue(result.asBoolean());
    }


    @Test
    public void IsMemberExisting() {
        var program = """
                (define object (eval-source "js" "a = { id: 2, method: x => x+1 }"))
                (is-member-existing? object "id")
                """;

        var result = context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void IsMemberNotExisting() {
        var program = """
                (define object (eval-source "js" "a = { id: 2, method: x => x+1 }"))
                (is-member-existing? object "name")
                """;

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }

    @Test
    public void IsMemberInsertable() {
        var program = """
                (define object (eval-source "js" "a = { id: 2, method: x => x+1 }"))
                (is-member-insertable? object "name")
                """;

        var result = context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void IsMemberNotInsertable() {
        var program = """
                (define object (eval-source "js" "a = { id: 2, method: x => x+1 }"))
                (is-member-insertable? object "id")
                """;

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }

    @Test
    public void IsMemberRemovable() {
        var program = """
                (define object (eval-source "js" "a = { id: 2, method: x => x+1 }"))
                (is-member-removable? object "id")
                """;

        var result = context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void IsMemberNotRemovable() {
        var program = """
                (define object (eval-source "js" "a = { id: 2, method: x => x+1 }"))
                (is-member-removable? object "test")
                """;

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }

    @Test
    public void IsMemberInvocable() {
        var program = """
                (define object (eval-source "js" "a = { id: 2, method: x => x+1 }"))
                (is-member-invocable? object "method")
                """;

        var result = context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void IsMemberWritableExistingMember() {
        var program = """
                (define object (eval-source "js" "a = { id: 2, method: x => x+1 }"))
                (is-member-writable? object "id")
                """;

        var result = context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void IsMemberWritableNotExistingMember() {
        var program = """
                (define object (eval-source "js" "a = { id: 2, method: x => x+1 }"))
                (is-member-writable? object "name")
                """;

        var result = context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

    @Test
    public void writeNewMember() {
        var program = """
                (define object (eval-source "js" "a = { id: 2, method: x => x+1 }"))
                                
                (if (is-member-insertable? object "name")
                    (write-member object "name" "Ivo Horak")
                    (read-member object "name"))
                                
                (read-member object "name")
                """;

        var result = context.eval("scm", program);

        assertEquals("Ivo Horak", result.asString());
    }

    @Test
    public void writeMemberCalledWithWrongNumberOfArgs() {
        var program = """
                (define object (eval-source "js" "a = { id: 2, method: x => x+1 }"))
                                
                (if (is-member-insertable? object "name")
                    (write-member "name" "Ivo Horak")
                    (read-member object "name"))
                                
                """;

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                write-member: arity mismatch; Expected number of arguments does not match the given number
                expected: 3
                given: 2""", msg);
    }


    @Test
    public void removeMember() {
        var program = """
                (define object (eval-source "js" "a = { id: 2, method: x => x+1 }"))
                                
                (if (is-member-removable? object "method")
                    (remove-member object "method")
                    #f)
                                
                object
                """;

        var result = context.eval("scm", program);

        assertTrue(result.hasMembers());
        assertEquals(1, result.getMemberKeys().size());

    }


    @Test
    public void invokeMemberWithOneArgument() {
        var program = """
                (define object (eval-source "js" "a = { id: 2, method: x => x+1 }"))
                                
                (if (is-member-invocable? object "method")
                    (invoke-member object "method" 1)
                    #f)
                """;

        var result = context.eval("scm", program);

        assertEquals(2L, result.asLong());
    }

    @Test
    public void invokeMemberWithNoArgument() {
        var program = """
                (define object (eval-source "js" "a = { id: 2, method: () => 1 }"))
                                
                (if (is-member-invocable? object "method")
                    (invoke-member object "method")
                    #f)
                """;

        var result = context.eval("scm", program);

        assertEquals(1L, result.asLong());
    }
}
