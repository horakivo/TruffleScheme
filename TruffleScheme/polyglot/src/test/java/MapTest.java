import org.graalvm.polyglot.Context;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MapTest {

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
    public void mapWithPrimitiveProcedureWorksWithForeignArrays() {
        var program = """
                (map + (eval-source "js" "[1, 2, 3]") (eval-source "js" "[4, 5, 6]"))
                """;

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(3, result.getArraySize());
        assertEquals(5L, result.getArrayElement(0).asLong());
        assertEquals(7L, result.getArrayElement(1).asLong());
        assertEquals(9L, result.getArrayElement(2).asLong());
    }

    @Test
    public void mapWithUserDefinedProcedureWorksWithForeignArrays() {
        var program = """
                (map (lambda (a b) (+ a b)) (eval-source "js" "[1, 2, 3]") (eval-source "js" "[4, 5, 6]"))
                """;

        var result = context.eval("scm", program);

        assertTrue(result.hasArrayElements());
        assertEquals(3, result.getArraySize());
        assertEquals(5L, result.getArrayElement(0).asLong());
        assertEquals(7L, result.getArrayElement(1).asLong());
        assertEquals(9L, result.getArrayElement(2).asLong());
    }
}
