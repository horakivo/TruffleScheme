import org.graalvm.polyglot.Context;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AppendTest {

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
    public void javascriptArrayAppend() {
        var program = """
                (append (eval-source "js" "[1, 2, 3]") (eval-source "js" "[4, 5, 6]"))
                """;

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(6, result.getArraySize());
        assertEquals(1L, result.getArrayElement(0).asLong());
        assertEquals(2L, result.getArrayElement(1).asLong());
        assertEquals(3L, result.getArrayElement(2).asLong());
        assertEquals(4L, result.getArrayElement(3).asLong());
        assertEquals(5L, result.getArrayElement(4).asLong());
        assertEquals(6L, result.getArrayElement(5).asLong());
    }

    @Test
    public void javascriptAndPythonArrayAppend() {
        var program = """
                (append (eval-source "js" "[1, 2, 3]") (eval-source "python" "[4, 5, 6]"))
                """;

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(6, result.getArraySize());
        assertEquals(1L, result.getArrayElement(0).asLong());
        assertEquals(2L, result.getArrayElement(1).asLong());
        assertEquals(3L, result.getArrayElement(2).asLong());
        assertEquals(4L, result.getArrayElement(3).asLong());
        assertEquals(5L, result.getArrayElement(4).asLong());
        assertEquals(6L, result.getArrayElement(5).asLong());
    }
}
