import org.graalvm.polyglot.Context;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GlobalScopeTests {

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
    public void pythonFib() {
        var program = """
                (eval-source "python" "def fibonacci(n):
                                           if n in {0, 1}:
                                               return n
                                           return fibonacci(n - 1) + fibonacci(n - 2)")
                                
                                
                (define test
                    (lambda (n)
                        ((p-proc "python" "fibonacci") n)))
                                
                                
                (test 10)
                """;

        var result = context.eval("scm", program);

        assertEquals(55L, result.asLong());
    }

    @Test
    public void test() {
        var program = """
                (eval-source "python" "a = 1")
                                
                                
                (+ 1 (p-proc "python" "a"))
                """;

        var result = context.eval("scm", program);

        assertEquals(2L, result.asLong());
    }
}
