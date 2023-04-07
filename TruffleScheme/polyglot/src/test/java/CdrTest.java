import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CdrTest {

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
    public void javascriptArray() {
        var program = """
                (cdr (eval-source "js" "[1, 2, 42]"))
                """;

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(2, result.getArraySize());
        assertEquals(2, result.getArrayElement(0).asInt());
        assertEquals(42, result.getArrayElement(1).asInt());
    }

    @Test
    public void pythonArray() {
        var program = """
                (cdr (eval-source "python" "[1, 2, 42]"))
                """;

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(2, result.getArraySize());
        assertEquals(2, result.getArrayElement(0).asInt());
        assertEquals(42, result.getArrayElement(1).asInt());
    }

    @Test
    public void throwException() {
        var program = """
                (cdr (eval-source "js" "a = { id: 2 }"))
                """;

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();
        assertTrue(msg.contains("""
                cdr: contract violation
                expected: pair? or list?
                given: com.oracle.truffle.js.runtime.objects.JSOrdinaryObject"""));
    }
}
