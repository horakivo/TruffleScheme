(eval-source "python" "def fibonacci(n):
                           if n in {0, 1}:
                               return n
                           return fibonacci(n - 1) + fibonacci(n - 2)")


(define python-fib (read-global-scope "python" "fibonacci"))


 (python-fib 35)
 (python-fib 35)
 (python-fib 35)
 (python-fib 35)
 (python-fib 35)
 (python-fib 35)
 (python-fib 35)
 (python-fib 35)
 (python-fib 35)
 (python-fib 35)
 (python-fib 35)

 (define start (current-milliseconds))

 (python-fib 35)

 (define end (current-milliseconds))
 (- end start)
