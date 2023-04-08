import org.graalvm.polyglot.Context;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LengthTest {
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
    public void javascriptLengthArray() {
        var program = """
                (length (eval-source "js" "[1, 2, 42]"))
                """;

        var result = context.eval("scm", program);

        assertEquals(3L, result.asLong());
    }

    @Test
    public void pythonLengthArray() {
        var program = """
                (length (eval-source "python" "[1, 2, 42]"))
                """;

        var result = context.eval("scm", program);

        assertEquals(3L, result.asLong());
    }
}
