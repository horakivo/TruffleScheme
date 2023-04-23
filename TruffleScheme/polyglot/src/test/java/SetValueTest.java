import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SetValueTest {

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
    public void setValue() {
        var program = """
                (define object (eval-source "js" "a = { id: 2, method: x => x+1 }"))
                                
                (set-value! id object 5)
                                
                (. id object)
                """;

        var result = context.eval("scm", program);

        assertEquals(5L, result.asLong());
    }

    @Test
    public void setValueWrongNumberOfArgsThrowsException() {
        var program = """
                (define object (eval-source "js" "a = { id: 2, method: x => x+1 }"))
                                
                (set-value! id object)
                                
                (. id object)
                """;

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("""
                set-value!: arity mismatch; Expected number of arguments does not match the given number
                expected: 3
                given: 2""", msg);
    }

    @Test
    public void setValueOnNonExistingMember() {
        var program = """
                (define object (eval-source "js" "a = { id: 2, method: x => x+1 }"))
                                
                (set-value! ivo object 5)
                                
                (. id object)
                """;

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertTrue(msg.contains("The slot 'ivo is missing from com.oracle.truffle.js"));
    }
}
