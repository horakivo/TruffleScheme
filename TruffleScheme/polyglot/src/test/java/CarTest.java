import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CarTest {

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
                (car (eval-source "js" "[1, 2, 42]"))
                """;

        var result = context.eval("scm", program);

        assertEquals(1L, result.asLong());
    }

    @Test
    public void pythonArray() {
        var program = """
                (car (eval-source "python" "[1, 2, 42]"))
                """;

        var result = context.eval("scm", program);

        assertEquals(1L, result.asLong());
    }

    @Test
    public void throwException() {
        var program = """
                (car (eval-source "js" "a = { id: 2 }"))
                """;

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();
        assertTrue(msg.contains("""
                car: contract violation
                expected: pair? or list?
                given: com.oracle.truffle.js.runtime.objects.JSOrdinaryObject"""));
    }
}
