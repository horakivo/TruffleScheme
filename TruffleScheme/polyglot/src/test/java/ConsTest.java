import org.graalvm.polyglot.Context;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConsTest {
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
    public void javascriptCons() {
        var program = """
                (cons 1 (eval-source "js" "[2, 3, 4]"))
                """;

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(4, result.getArraySize());
        assertEquals(1L, result.getArrayElement(0).asLong());
        assertEquals(2L, result.getArrayElement(1).asLong());
        assertEquals(3L, result.getArrayElement(2).asLong());
        assertEquals(4L, result.getArrayElement(3).asLong());
    }


    @Test
    public void pythonCons() {
        var program = """
                (cons 1 (eval-source "python" "[2, 3, 4]"))
                """;

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(4, result.getArraySize());
        assertEquals(1L, result.getArrayElement(0).asLong());
        assertEquals(2L, result.getArrayElement(1).asLong());
        assertEquals(3L, result.getArrayElement(2).asLong());
        assertEquals(4L, result.getArrayElement(3).asLong());
    }

    @Test
    public void javascriptAndPythonArrayAppend() {
        var program = """
                (cons (eval-source "js" "[1, 2, 3]") (eval-source "python" "[2, 3, 4]"))
                """;

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(4, result.getArraySize());
        assertTrue(result.getArrayElement(0).hasArrayElements());
        assertEquals(3, result.getArrayElement(0).getArraySize());
        assertEquals(2L, result.getArrayElement(1).asLong());
        assertEquals(3L, result.getArrayElement(2).asLong());
        assertEquals(4L, result.getArrayElement(3).asLong());
    }
}
