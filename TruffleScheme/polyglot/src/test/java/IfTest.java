import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class IfTest {

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
    public void supportForeignObject() {
        var program = """
                (if (eval-source "js" "[2, 3, 4]") 1 2)
                """;

        var result = context.eval("scm", program);

        assertEquals(1L, result.asLong());
    }
}
