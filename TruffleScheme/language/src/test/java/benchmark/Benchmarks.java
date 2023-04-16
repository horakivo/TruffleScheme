package benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public class Benchmarks extends TruffleBenchmark {


//    @Benchmark
//    public void fib(Blackhole blackhole) {
//        var program = """
//                (define fibonacci
//                  (lambda (n)
//                      (if (< n 2)
//                          1
//                          (+ (fibonacci (- n 1))
//                             (fibonacci (- n 2))))))
//
//
//
//                (fibonacci 35)
//                """;
//        var result = truffleContext.eval("scm", program);
//        blackhole.consume(result);
//    }
//
//    @Benchmark
//    public void fak(Blackhole blackhole) {
//        var program = """
//                (define fak
//                  (lambda (x)
//                    (define iter
//                      (lambda (n result)
//                        (if (= n 0)
//                          result
//                          (iter (- n 1) (+ n result)))))
//
//                    (iter x 1)))
//
//                (fak 10000000)
//                """;
//        var result = truffleContext.eval("scm", program);
//        blackhole.consume(result);
//    }
//
//    @Benchmark
//    public void quicksort(Blackhole blackhole) {
//        var program = """
//                (define pivot
//                  (lambda (l)
//                    (cond ((null? l) 'done)
//                          ((null? (cdr l)) 'done)
//                          ((<= (car l) (car (cdr l))) (pivot (cdr l)))
//                          (#t (car l)))))
//
//
//
//                (define partition
//                  (lambda (piv l p1 p2)
//                    (if (null? l) (list p1 p2)
//                        (if (< (car l) piv)
//                            (partition piv (cdr l) (cons (car l) p1) p2)
//                            (partition piv (cdr l) p1 (cons (car l) p2))))))
//
//                (define quicksort1
//                  (lambda (l)
//                    (let ((piv (pivot l)))
//                      (if (equal? piv 'done)
//                          l
//                          (let ((parts (partition piv l '() '())))
//                            (append (quicksort1 (car parts))
//                                    (quicksort1 (car (cdr parts)))))))))
//
//                (define random-list
//                  (lambda (len)
//                    (generate len 101 17 3 '())))
//
//
//                (define generate
//                  (lambda (len p q s result)
//                    (if (= len 0)
//                        result
//                        (tmp (modulo (* s s) (* p q)) len p q result))))
//
//
//                (define tmp
//                  (lambda (value len p q result)
//                           (generate (- len 1) p q value (cons value result))))
//
//
//
//                (quicksort1 (random-list 500000))
//                """;
//        var result = truffleContext.eval("scm", program);
//        blackhole.consume(result);
//    }

    @Benchmark
    public void tak(Blackhole blackhole) {
        var program = """
                (define tak
                  (lambda (x y z)
                    (if (not (< y x))
                        z
                        (tak (tak (- x 1) y z)
                           (tak (- y 1) z x)
                           (tak (- z 1) x y)))))
                                
                                
                (define loop
                  (lambda (n)
                    (tak 18 12 6)
                    (if (> n 0) (loop (- n 1)))))
                    
                (loop 100)
                """;
        var result = truffleContext.eval("scm", program);
        blackhole.consume(result);
    }

//    @Benchmark
//    public void randomList(Blackhole blackhole) {
//        var program = """
//                (define random-list
//                  (lambda (len)
//                    (generate len 101 17 3 '())))
//
//
//                (define generate
//                  (lambda (len p q s result)
//                    (if (= len 0)
//                        result
//                        (tmp (modulo (* s s) (* p q)) len p q result))))
//
//
//                (define tmp
//                  (lambda (value len p q result)
//                           (generate (- len 1) p q value (cons value result))))
//
//                (random-list 500000)
//                """;
//
//        var result = truffleContext.eval("scm", program);
//        blackhole.consume(result);
//    }
}
