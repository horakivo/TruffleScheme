import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EqualTest {

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
    public void equalIsNotSupportedForForeignArrays() {
        var program = """
                (equal? (eval-source "js" "[2, 3, 4]") (eval-source "js" "[2, 3, 4]"))
                """;

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("Equal? is not supported for foreign arrays", msg);
    }

}
