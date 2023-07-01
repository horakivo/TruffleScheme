;;; FIB -- A classic benchmark, computes fib(35) inefficiently.

(define fibonacci
  (lambda (n)
      (if (< n 2)
          n
          (+ (fibonacci (- n 1))
             (fibonacci (- n 2))))))
           
(fibonacci 35)
