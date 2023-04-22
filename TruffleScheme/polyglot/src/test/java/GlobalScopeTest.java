import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class GlobalScopeTest {

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
    public void procedureDefinitionInPython() {
        var program = """
                (eval-source "python" "def fibonacci(n):
                                           if n in {0, 1}:
                                               return n
                                           return fibonacci(n - 1) + fibonacci(n - 2)")
                                             
                (define test
                    (lambda (n)
                        ((read-global-scope "python" "fibonacci") n)))
                                             
                (test 10)
                """;

        var result = context.eval("scm", program);

        assertEquals(55L, result.asLong());
    }

    @Test
    public void procedureDefinitionInJavaScript() {
        var program = """
                (eval-source "js" "function fib(n) {
                                       if (n < 2) {
                                           return n;
                                       }
                                       return fib(n - 1) + fib(n - 2);
                                   }")
                                             
                ((read-global-scope "js" "fib") 10)
                """;

        var result = context.eval("scm", program);

        assertEquals(55L, result.asLong());
    }

    @Test
    public void variableDefinedInPython() {
        var program = """
                (eval-source "python" "a = 1")
                                             
                (+ 1 (read-global-scope "python" "a"))
                """;

        var result = context.eval("scm", program);

        assertEquals(2L, result.asLong());
    }


    @Test
    public void redefinitionOfGlobalScopeWorks() {
        var program = """
                (eval-source "python" "a = 1")
                (define a (read-global-scope "python" "a"))
                (eval-source "python" "a = 2")
                (define b (read-global-scope "python" "a"))
                                           
                (list a b)
                """;

        var result = context.eval("scm", program);

        assertEquals(1L, result.getArrayElement(0).asLong());
        assertEquals(2L, result.getArrayElement(1).asLong());
    }

    @Test
    public void throwsExceptionWhenWrongArgumentTypeIsGiven() {
        var program = """
                (eval-source "python" "a = 1")
                                             
                (+ 1 (read-global-scope 'a "a"))
                """;

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                read-global-scope: contract violation
                expected: string?
                given: 'a""",msg);
    }
}
