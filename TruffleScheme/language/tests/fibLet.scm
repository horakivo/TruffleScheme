(define fibonacci
  (lambda (n)
    (let ((number n))
      (if (< number 2)
          number
          (+ (fibonacci (- number 1))
             (fibonacci (- number 2)))))))

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