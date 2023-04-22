import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EvalSourceTest {


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
    public void pythonLambda() {
        var program = """
                (define python-proc (eval-source "python" "lambda a, b: a + b"))
                                           
                (python-proc 1 2)
                """;

        var result = context.eval("scm", program);

        assertEquals(3L, result.asLong());
    }

    @Test
    public void throwsExceptionWhenWrongArgumentTypeIsGiven() {
        var program = """
                (eval-source 5 "a = 1")
                """;

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                eval-source: contract violation
                expected: string?
                given: 5""", msg);
    }

    @Test
    public void throwsExceptionWhenWrongArgumentTypeIsGivenInSourceCode() {
        var program = """
                (eval-source "python" 'test)
                """;

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                eval-source: contract violation
                expected: string?
                given: 'test""", msg);
    }
}
