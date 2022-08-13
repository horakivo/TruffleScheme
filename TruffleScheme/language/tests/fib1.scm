(define fibonacci
  (lambda (n)
    (if (< n 2)
        n
        (+ (fibonacci (- n 1))
           (fibonacci (- n 2))))))
           

(define start (current-milliseconds))

(fibonacci 32)

(define end (current-milliseconds))
(- end start)