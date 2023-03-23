package benchmark;

import org.graalvm.polyglot.Value;
import org.openjdk.jmh.annotations.Benchmark;

public class FibBenchmark extends TruffleBenchmark {


    @Benchmark
    public long fib() {
        var program = """
                (define fibonacci
                  (lambda (n)
                      (if (< n 2)
                          1
                          (+ (fibonacci (- n 1))
                             (fibonacci (- n 2))))))



                (fibonacci 20)
                """;
        return truffleContext.eval("scm", program).asLong();
    }
//
//    @Benchmark
//    public int fibJS() {
//        final String FIBONACCI_JS_FUNCTION = "" +
//                "function fib(n) { " +
//                "    if (n < 2) { " +
//                "        return 1; " +
//                "    } " +
//                "    return fib(n - 1) + fib(n - 2); " +
//                "} " + "fib(35);";
//        return truffleContext.eval("js", FIBONACCI_JS_FUNCTION).asInt();
//    }
}
