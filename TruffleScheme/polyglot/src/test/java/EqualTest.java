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

    // for foreign object we do equals based on the reference

    @Test
    public void twoEqualArraysReturnFalse() {
        var program = """
                (equal? (eval-source "js" "[2, 3, 4]") (eval-source "js" "[2, 3, 4]"))
                """;

        var result = context.eval("scm", program);

        assertFalse(result.asBoolean());
    }

    @Test
    public void twoSameReferencesReturnTrue() {
        var program = """
                (define a (eval-source "js" "[2, 3, 4]"))
                (equal? a a)
                """;

        var result = context.eval("scm", program);

        assertTrue(result.asBoolean());
    }

}
