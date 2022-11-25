(define countdown
  (lambda (n)
    (if (= 0 n)
        0
        (countdown (- n 1)))))

(infinite (countdown 3))