import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IsNullTest {

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
    public void javascriptNull() {
        var program = """
                (null? (eval-source "js" "null"))
                """;

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }

    @Test
    public void javascriptIsNotNull() {
        var program = """
                (null? (eval-source "js" "a = { id: 2 }"))
                """;

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }

    @Test
    public void pythonNull() {
        var program = """
                (null? (eval-source "python" "None"))
                """;

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }
}
