(define fibonacci
  (lambda (n)
    (cond ((< n 2) n)
          (else (+ (fibonacci (- n 1)) (fibonacci (- n 2)))))))

(fibonacci 32)
(fibonacci 32)
(fibonacci 32)
(fibonacci 32)
(fibonacci 32)
(fibonacci 32)
(fibonacci 32)
(fibonacci 32)
(fibonacci 32)
(fibonacci 32)
(fibonacci 32)
(fibonacci 32)
(fibonacci 32)
(fibonacci 32)
(fibonacci 32)

(define start (current-milliseconds))

(fibonacci 32)

(define end (current-milliseconds))
(- end start)