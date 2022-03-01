(define fibonacci
  (lambda (n)
    (if (< n 2)
        1
        (+ (fibonacci (- n 1))
           (fibonacci (- n 2))))))
           
(fibonacci 30)
(fibonacci 30)
(fibonacci 30)
(fibonacci 30)
(fibonacci 30)
(fibonacci 30)
(fibonacci 30)

(define start (current-milliseconds))

(fibonacci 30)

(define end (current-milliseconds))
(- end start)