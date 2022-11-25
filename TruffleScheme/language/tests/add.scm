(define fibonacci
  (lambda (n)
      (if (< n 2)
          n
          (+ (fibonacci (- n 1))
             (fibonacci (- n 2))))))

(infinite (fibonacci 14))
