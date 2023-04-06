(eval-source "python" "def fibonacci(n):
                           if n in {0, 1}:
                               return n
                           return fibonacci(n - 1) + fibonacci(n - 2)")


(define test
    (lambda (n)
        ((p-proc 'python 'fibonacci) n)))


(test 10)


