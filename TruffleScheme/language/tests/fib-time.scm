(define fibonacci
  (lambda (n)
      (if (< n 2)
          n
          (+ (fibonacci (- n 1))
             (fibonacci (- n 2))))))
           
(fibonacci 35)
(fibonacci 35)
(fibonacci 35)
(fibonacci 35)
(fibonacci 35)
(fibonacci 35)
(fibonacci 35)
(fibonacci 35)
(fibonacci 35)
(fibonacci 35)
(fibonacci 35)
(fibonacci 35)
(fibonacci 35)
(fibonacci 35)
(fibonacci 35)
(fibonacci 35)
(fibonacci 35)

(define start (current-milliseconds))

(fibonacci 35)

(define end (current-milliseconds))
(- end start)