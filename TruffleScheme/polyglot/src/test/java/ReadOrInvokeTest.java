import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReadOrInvokeTest {

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
    public void jsDotSyntaxIsWorkingForInvokeAndRead() {
        var program = """
                (eval-source "js" "class Rectangle {
                                      constructor(height, width) {
                                        this.height = height;
                                        this.width = width;
                                      }

                                      calcArea() {
                                        return this.height * this.width;
                                      }
                                    }
                                    const b = new Rectangle(10, 10)")

                (define object (read-global-scope "js" "b"))
                (define height (. height object))
                (define area (. calcArea object))

                (list height area)
                """;

        var result = context.eval("scm", program);

        assertEquals(10L, result.getArrayElement(0).asLong());
        assertEquals(100L, result.getArrayElement(1).asLong());
    }

    @Test
    public void pythonDotSyntaxIsWorkingForInvokeAndRead() {
        var program = """
                (eval-source "python"
                "class Rectangle:
                     def __init__(self, height, width):
                         self.height = height
                         self.width = width

                     def calcArea(self):
                         return self.height * self.height

                p1 = Rectangle(10, 10)")

                (define object (read-global-scope "python" "p1"))


                (define height (. height object))
                (define area (. calcArea object))
                (list height area)
                """;

        var result = context.eval("scm", program);

        assertEquals(10L, result.getArrayElement(0).asLong());
        assertEquals(100L, result.getArrayElement(1).asLong());
    }

    @Test
    public void pythonCallingFieldWithArgumentsThrowsError() {
        var program = """
                (eval-source "python"
                "class Rectangle:
                     def __init__(self, height, width):
                         self.height = height
                         self.width = width

                     def calcArea(self):
                         return self.height * self.height

                p1 = Rectangle(10, 10)")

                (define object (read-global-scope "python" "p1"))


                (define height (. height object 1))
                """;

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertTrue(msg.contains("Message is not supported"));
    }

    @Test
    public void pythonCallingWithWrongNumberOfArgsThrowException() {
        var program = """
                (eval-source "python"
                "class Rectangle:
                     def __init__(self, height, width):
                         self.height = height
                         self.width = width

                     def calcArea(self):
                         return self.height * self.height

                p1 = Rectangle(10, 10)")

                (define object (read-global-scope "python" "p1"))


                (define height (. calcArea object 1))
                """;

        var msg = assertThrows(PolyglotException.class, () -> context.eval("scm", program)).getMessage();

        assertEquals("TypeError: calcArea() takes 1 positional argument but 2 were given", msg);
    }
}
