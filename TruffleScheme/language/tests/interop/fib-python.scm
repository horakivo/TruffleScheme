(eval-source "python" "def fibonacci(n):
                           if n in {0, 1}:
                               return n
                           return fibonacci(n - 1) + fibonacci(n - 2)")

(define python-fib (read-global-scope "python" "fibonacci"))

(python-fib 35)

