import org.graalvm.polyglot.Context;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ApplyTest {


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
    public void applyWithPrimitiveProcedureWorksWithForeignArray() {
        var program = """
                (apply - (eval-source "js" "[1, 2, 3]"))
                """;

        var result = context.eval("scm", program);

        assertEquals(-4L, result.asLong());
    }

    @Test
    public void applyWithPrimitiveProcedureAndOptionalArgsWorksWithForeignArray() {
        var program = """
                (apply - 1 2 (eval-source "js" "[1, 2, 3]"))
                """;

        var result = context.eval("scm", program);

        assertEquals(-7L, result.asLong());
    }

    @Test
    public void applyWithUserDefinedProcedureWorksWithForeignArray() {
        var program = """
                (apply (lambda (a b) (+ a b)) (eval-source "js" "[1, 2]"))
                """;

        var result = context.eval("scm", program);

        assertEquals(3L, result.asLong());
    }

    @Test
    public void applyWithUserDefinedProcedureAndOptionalArgsWorksWithForeignArray() {
        var program = """
                (apply (lambda (a b c) (+ a b c)) 1 (eval-source "js" "[2, 3]"))
                """;

        var result = context.eval("scm", program);

        assertEquals(6L, result.asLong());
    }
}
